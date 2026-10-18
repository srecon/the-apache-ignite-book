package com.blu.imdg.example7;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.example3.ValidateMessage;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Tests the XSD validation service deployment and execution.
 */
public class TestXsdValidatingService {
    private static final Logger log = LoggerFactory.getLogger(TestXsdValidatingService.class);

    private static final String VALIDATING_SERVICE = "validatingService";

    public static void main(String[] args) throws IOException {
        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            var sample1 = TestDataGenerator.getSample1();
            var sample2 = TestDataGenerator.getSample2();
            var validateSchema = TestDataGenerator.getValidateSchema();
            var validateScript = TestDataGenerator.getValidateScript();

            ignite.services().deployNodeSingleton(VALIDATING_SERVICE, new XsdValidatingServiceImpl());

            var xsdValidatingService = ignite.services().serviceProxy(VALIDATING_SERVICE, XsdValidatingService.class, /*not-sticky*/false);

            log.info("result={}", xsdValidatingService.isOk(new ValidateMessage("1", sample1, validateSchema, validateScript)));
            log.info("result2={}", xsdValidatingService.isOk(new ValidateMessage("2", sample2, validateSchema, validateScript)));

            ignite.services().cancel(VALIDATING_SERVICE);
        }

    }
}
