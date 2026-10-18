package com.blu.imdg.example6;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSessionFullSupport;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.apache.ignite.configuration.CacheConfiguration;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.example3.ValidateMessage;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.checkpoint.cache.CacheCheckpointSpi;
import org.apache.ignite.spi.failover.always.AlwaysFailoverSpi;
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
 * ForkJoin computation with checkpoint support for fault-tolerant distributed execution.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example6.ForkJoinWithCheckpointComputation
 */
@ComputeTaskSessionFullSupport
public class ForkJoinWithCheckpointComputation extends ComputeTaskSplitAdapter<ValidateMessage[], Boolean> {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinWithCheckpointComputation.class);

    @Override
    protected Collection<? extends ComputeJob> split(int i, ValidateMessage[] messages) throws IgniteException {
        return Arrays.stream(messages).map(ForkJoinWithCheckpointJobAdapter::new).collect(Collectors.toList());
    }


    @Nullable
    @Override
    public Boolean reduce(List<ComputeJobResult> results) throws IgniteException {
        return results.stream().reduce(true, (acc, value) -> acc && (Boolean) value.getData(), (a, b) -> a && b);
    }

    public static void main(String[] args) throws IOException {

        try (var ignite = Ignition.start(CLIENT_CONFIG)) {
            var compute = ignite.compute();
            var cacheConfiguration = new CacheConfiguration("checkpoints");
            // explicitly uses of checkpoint
            var cacheCheckpointSpi = new CacheCheckpointSpi();
            cacheCheckpointSpi.setCacheName("checkpointCache");
            //cacheConfiguration.setC
            ignite.configuration().setCheckpointSpi(cacheCheckpointSpi)
                                  .setFailoverSpi(new AlwaysFailoverSpi());
            // create or get cache
            ignite.getOrCreateCache(cacheConfiguration);

            var validateMessages = TestDataGenerator.getValidateMessages();

            var result = compute.execute(new ForkJoinWithCheckpointComputation(), validateMessages);
            log.info("final result={}", result);
        }
    }
}
