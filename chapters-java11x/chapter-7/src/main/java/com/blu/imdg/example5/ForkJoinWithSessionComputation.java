package com.blu.imdg.example5;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSessionFullSupport;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.example3.ValidateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.blu.imdg.common.CommonConstants.CLIENT_CONFIG;

/**
 * ForkJoin computation with session full support and checkpointing.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example5.ForkJoinWithSessionComputation
 */
@ComputeTaskSessionFullSupport
public class ForkJoinWithSessionComputation extends ComputeTaskSplitAdapter<ValidateMessage[], Boolean> {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinWithSessionComputation.class);

    @Override
    protected Collection<? extends ComputeJob> split(int i, ValidateMessage[] messages) throws IgniteException {
        return Arrays.stream(messages).map(ForkJoinWithSessionJobAdapter::new).collect(Collectors.toList());
    }


    @Nullable
    @Override
    public Boolean reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.stream().reduce(true, (acc, value) -> acc && (Boolean) value.getData(), (a, b) -> a && b);
    }

    public static void main(String[] args) throws IOException {
        try (var ignite = Ignition.start(CLIENT_CONFIG)) {
            var compute = ignite.compute();

            var validateMessages = TestDataGenerator.getValidateMessages();

            var result = compute.execute(new ForkJoinWithSessionComputation(), validateMessages);
            log.info("result={}", result);
        }
    }
}
