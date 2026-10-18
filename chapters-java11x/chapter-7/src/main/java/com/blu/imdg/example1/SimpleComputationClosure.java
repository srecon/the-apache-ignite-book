package com.blu.imdg.example1;

import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.common.XsdValidator;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteClosure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.blu.imdg.common.CommonConstants.CLIENT_CONFIG;

/**
 * Demonstrates Ignite closure execution with a lambda on all cluster nodes.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example1.SimpleComputationClosure
 */
public class SimpleComputationClosure {
    private static final Logger log = LoggerFactory.getLogger(SimpleComputationClosure.class);

    public static void main(String[] args) throws IOException {
        try (var ignite = Ignition.start(CLIENT_CONFIG)) {
            var compute = ignite.compute();

            // Execute closure on all cluster nodes.
            var words = List.of("Count characters using closure");
            var res = compute.apply(
                    (IgniteClosure<String, Integer>) word -> word.length(),
                    words
            );

            int sum = res.stream().reduce(0, Integer::sum);
            log.info("Length of the sentence: {}", sum);
        }
    }
}
