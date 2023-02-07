package com.blu.imdg.common;


import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 */
public class JSEvaluate {

    public static boolean evaluateJs(String xml, String js) {
        System.out.println("Using graal JavaScript engine!!!");

        ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        System.out.println("Engine Name:" + graalEngine.toString());

        Bindings bindings = graalEngine.createBindings();

        try {
            bindings.put("polyglot.js.allowHostAccess", true); // essential for evulate on JVM 17
            bindings.put("xpath", new XPathExecutor(xml));
            return (boolean) graalEngine.eval(js, bindings);
        } catch (final Exception err) {
            throw new RuntimeException(err);
        }
    }

}
