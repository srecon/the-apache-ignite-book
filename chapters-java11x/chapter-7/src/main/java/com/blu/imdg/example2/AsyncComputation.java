package com.blu.imdg.example2;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import com.blu.imdg.common.TestDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Demonstrates async computation with CompletableFuture in Ignite.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example2.AsyncComputation
 */
public class AsyncComputation {
    private static final Logger log = LoggerFactory.getLogger(AsyncComputation.class);

    public static void main(String[] args) throws IOException {
        var sample1 = TestDataGenerator.getSample1();
        var validateSchema = TestDataGenerator.getValidateSchema();
        var validateScript = TestDataGenerator.getValidateScript();

        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {
            var compute = ignite.compute();

            var resultFuture = CompletableFuture.supplyAsync(() -> {
                return compute.call(() -> {
                    var validateXsdResult = XsdValidator.validate(sample1, validateSchema);
                    var validateByJs = JSEvaluate.evaluateJs(sample1, validateScript);

                    log.info("validateXsdResult={}, validateByJs={}", validateXsdResult, validateByJs);

                    return validateXsdResult && validateByJs;
                });
            });

            resultFuture.thenAccept(res ->
                    log.info("result={}", res)
            );

            // Block until the async computation completes
            var result = resultFuture.get();
            log.info("Final result: {}", result);

            //System.out.println("Press ENTER to exit!");
            //System.in.read();
        }
    }

}
