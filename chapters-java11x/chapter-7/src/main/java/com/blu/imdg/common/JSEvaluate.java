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
//        ScriptEngineManager factory = new ScriptEngineManager();
//        ScriptEngine engine = factory.getEngineByName("nashorn");
//        Bindings bindings = engine.createBindings();

        ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        System.out.println("Engine Name:" + graalEngine.toString());

        Bindings bindings = graalEngine.createBindings();

        try {
            graalEngine.eval("print('Hello World!')");
            bindings.put("xpath", new XPathExecutor(xml));
            return (boolean) graalEngine.eval(js, bindings);
        } catch (final Exception err) {
            throw new RuntimeException(err);
        }
    }

}
