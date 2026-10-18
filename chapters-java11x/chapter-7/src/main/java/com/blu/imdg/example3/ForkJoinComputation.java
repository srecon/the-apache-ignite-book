package com.blu.imdg.example3;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.TestDataGenerator;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.cluster.ClusterTopologyException;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.LoadBalancerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates ForkJoin task execution across Ignite cluster nodes.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example3.ForkJoinComputation
 */
public class ForkJoinComputation {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinComputation.class);

    public static void main(String[] args) throws IOException {

        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            var compute = ignite.compute();

            var validateMessages = TestDataGenerator.getValidateMessages();

            Boolean result = compute.execute(new ComputeTask<ValidateMessage[], Boolean>() {
                @LoadBalancerResource
                private ComputeLoadBalancer balancer;

                @Nullable
                @Override
                public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> nodes, @Nullable ValidateMessage[] messages) throws IgniteException {
                    var result = new java.util.HashMap<ComputeJob, ClusterNode>();
                    for (var msg : messages) {
                        var job = new ForkJoinJobAdapter(msg);
                        var balancedNode = balancer.getBalancedNode(job, null);
                        result.put(job, balancedNode);
                    }
                    return result;
                }

                @Override
                public ComputeJobResultPolicy result(ComputeJobResult computeJobResult, List<ComputeJobResult> results) throws IgniteException {
                    var e = computeJobResult.getException();
                    if (e != null) {
                        if (!(e instanceof ComputeExecutionRejectedException) && !(e instanceof ClusterTopologyException) && !e.hasCause(new Class[]{ComputeJobFailoverException.class})) {
                            throw new IgniteException("Remote job threw user exception (override or implement ComputeTask.result(..) method if you would like to have automatic failover for this exception).", e);
                        } else {
                            return ComputeJobResultPolicy.FAILOVER;
                        }
                    } else {
                        return ComputeJobResultPolicy.WAIT;
                    }
                }

                @Nullable
                @Override
                public Boolean reduce(List<ComputeJobResult> results) throws IgniteException {
                    return results.stream().reduce(true, (acc, value) -> acc && (Boolean) value.getData(), (a, b) -> a && b);
                }
            }, validateMessages);


            log.info("result={}", result);

        }

    }

}
