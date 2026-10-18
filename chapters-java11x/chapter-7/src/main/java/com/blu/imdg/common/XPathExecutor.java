package com.blu.imdg.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.function.Function;

/**
 * XPath executor for XML documents, used as a Java function in GraalVM JS evaluation.
 */
public class XPathExecutor implements Function<String, Object> {
    private static final Logger log = LoggerFactory.getLogger(XPathExecutor.class);
    private final Document document;

    public XPathExecutor(String xml) {
        log.debug("Parsing XML document for XPath queries");
        try {
            var dbf = DocumentBuilderFactory.newInstance();
            var db = dbf.newDocumentBuilder();
            document = db.parse(new InputSource(new StringReader(xml)));
        } catch (Exception err) {
            log.error("Failed to parse XML document", err);
            throw new RuntimeException(err);
        }
    }

    @Override
    public Object apply(String xpathStr) {
        try {
            var xpath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
            var result = xpath.evaluate(xpathStr, document);
            log.debug("XPath '{}' evaluated to: {}", xpathStr, result);
            return result;
        } catch (javax.xml.xpath.XPathExpressionException e) {
            log.error("XPath expression failed: {}", xpathStr, e);
            throw new RuntimeException(e);
        }
    }
}
