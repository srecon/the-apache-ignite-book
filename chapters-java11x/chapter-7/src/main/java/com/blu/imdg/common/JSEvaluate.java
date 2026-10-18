package com.blu.imdg.common;


import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Evaluates JavaScript using GraalVM polyglot. Modern Java 21 compatible.
 */
public class JSEvaluate {
    private static final Logger log = LoggerFactory.getLogger(JSEvaluate.class);

    public static boolean evaluateJs(String xml, String js) {
        log.info("Using GraalJS engine for evaluation");

        var graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        log.info("Engine: {}", graalEngine.toString());

        try (var context = Context.newBuilder("js")
                .allowHostAccess(org.graalvm.polyglot.HostAccess.EXPLICIT)
                .allowAllAccess(true)
                .build()) {

            // Create XPath helper via GraalVM polyglot
            var xpathExecutor = new XPathExecutor(xml);
            context.getBindings("js").putMember("xpath", context.getHostSpaceManager().createProxy(xpathExecutor, Object.class));

            var result = context.eval("js", js);
            log.debug("JavaScript evaluation result: {}", result);
            return result.asBoolean();
        } catch (Exception err) {
            log.error("JavaScript evaluation failed", err);
            throw new RuntimeException(err);
        }
    }

}
