package com.blu.imdg.example1;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.common.XsdValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.blu.imdg.common.CommonConstants.CLIENT_CONFIG;

/**
 * Simple computation: validates XML against XSD and evaluates JavaScript.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example1.SimpleComputation
 */
public class SimpleComputation {
    private static final Logger log = LoggerFactory.getLogger(SimpleComputation.class);

    public static void main(String[] args) throws IOException {
        var sample1 = TestDataGenerator.getSample1();
        var validateSchema = TestDataGenerator.getValidateSchema();
        var validateScript = TestDataGenerator.getValidateScript();

        try (var ignite = Ignition.start(CLIENT_CONFIG)) {
            var compute = ignite.compute();

            var result = compute.call(() -> {
                var validateXsdResult = XsdValidator.validate(sample1, validateSchema);
                var validateByJs = JSEvaluate.evaluateJs(sample1, validateScript);

                log.info("validateXsdResult={}, validateByJs={}", validateXsdResult, validateByJs);

                return validateXsdResult && validateByJs;
            });

            log.info("result={}", result);
        }
    }
}
