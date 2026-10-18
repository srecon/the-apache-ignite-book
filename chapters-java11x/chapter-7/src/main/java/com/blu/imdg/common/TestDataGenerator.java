package com.blu.imdg.common;

import org.apache.commons.io.IOUtils;
import com.blu.imdg.example3.ValidateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Test data generator for loading sample XML, XSD, and JS resources.
 */
public class TestDataGenerator {
    private static final Logger log = LoggerFactory.getLogger(TestDataGenerator.class);

    private static final String SAMPLE1 = "/META-INF/org/book/examples/data/sample1.xml";
    private static final String SAMPLE2 = "/META-INF/org/book/examples/data/sample2.xml";
    private static final String SAMPLE3 = "/META-INF/org/book/examples/data/sample3.xml";
    private static final String VALIDATE_SCHEMA = "/META-INF/org/book/examples/data/validate-schema.xsd";
    private static final String VALIDATE_SCRIPT = "/META-INF/org/book/examples/data/validate-script.js";

    private static String getStringResource(String resource) throws IOException {
        try (InputStream is = TestDataGenerator.class.getResourceAsStream(resource)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resource);
            }
            return IOUtils.toString(is, java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    private static byte[] getByteResource(String resource) throws IOException {
        try (InputStream is = TestDataGenerator.class.getResourceAsStream(resource)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resource);
            }
            return IOUtils.toByteArray(is);
        }
    }


    public static String getValidateScript() throws IOException {
        return getStringResource(VALIDATE_SCRIPT);
    }

    public static byte[] getValidateSchema() throws IOException {
        return getByteResource(VALIDATE_SCHEMA);
    }

    public static String getSample1() throws IOException {
        return getStringResource(SAMPLE1);
    }

    public static String getSample2() throws IOException {
        return getStringResource(SAMPLE2);
    }

    public static ValidateMessage[] getValidateMessages() throws IOException {
        var validateSchema = getValidateSchema();
        var validateScript = getValidateScript();

        log.info("Loading {} validation messages from resources", 3);
        return new ValidateMessage[]{
                new ValidateMessage("1", getStringResource(SAMPLE1), validateSchema, validateScript),
                new ValidateMessage("2", getStringResource(SAMPLE2), validateSchema, validateScript),
                new ValidateMessage("3", getStringResource(SAMPLE3), validateSchema, validateScript)
        };
    }


}
