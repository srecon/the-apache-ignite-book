package com.blu.imdg.example2;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import com.blu.imdg.common.TestDataGenerator;
import org.apache.ignite.lang.IgniteFuture;

import java.io.IOException;

/**
 *
 */
//mvn exec:java -Dexec.mainClass=com.blu.imdg.example2.AsyncComputation
public class AsyncComputation {

    public static void main(String[] args) throws IOException {
        String sample1 = TestDataGenerator.getSample1();
        byte[] vaidateSchema = TestDataGenerator.getValidateSchema();
        String validateScript = TestDataGenerator.getValidateScript();

        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {
            IgniteCompute compute = ignite.compute();

            IgniteFuture<Boolean> result = compute.callAsync(() -> {
                boolean validateXsdResult = XsdValidator.validate(sample1, vaidateSchema);
                boolean validateByJs = JSEvaluate.evaluateJs(sample1, validateScript);

                System.out.println("validateXsdResult=" + validateXsdResult);
                System.out.println("validateByJs=" + validateByJs);

                return validateXsdResult && validateByJs;
            });

            result.listen((r) -> {
                boolean res = (boolean) result.get();
                System.out.println("result=" + res);
            });

            result.get();

            //System.out.println("Presse ENTER to exit!");
            //System.in.read();
        }
    }

}
