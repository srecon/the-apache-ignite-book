package com.blu.imdg.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;

/**
 * Utility class for XSD validation of XML content.
 */
public class XsdValidator {
    private static final Logger log = LoggerFactory.getLogger(XsdValidator.class);

    public static boolean validate(String xml, byte[] xsd) {
        try (var baisXsd = new ByteArrayInputStream(xsd);
             var baisXml = new ByteArrayInputStream(xml.getBytes())) {

            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            var schemaFile = new StreamSource(baisXsd);
            var schema = factory.newSchema(schemaFile);

            var validator = schema.newValidator();
            validator.validate(new StreamSource(baisXml));
            log.debug("XML validation passed");
            return true;
        } catch (Exception err) {
            log.error("XML validation failed", err);
            return false;
        }
    }

}
