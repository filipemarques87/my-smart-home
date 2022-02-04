package io.mysmarthome.service.impl;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.script.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ScriptExecutor {

    private final ScriptEngine scriptEngine;
    private final List<Object> dataHistory;

    public ScriptExecutor(List<Object> dataHistory, ScriptEngine scriptEngine) {
        this.dataHistory = new ArrayList<>(dataHistory);
        this.scriptEngine = scriptEngine;
    }

    public Object execute(String script) {
        if (script.startsWith("file:")) {
            return executeScriptFromFile(script.substring("file:".length()));
        }
        return executeScriptFromText(script);
    }

    @SneakyThrows
    private Object executeScriptFromText(String script) {
        ScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
        Bindings engineScope = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
        engineScope.put("$v", dataHistory);
        return scriptEngine.eval(script, scriptContext);
    }

    // TODO dar a possibilidade de executar funcoes dentro do ficheiro
    @SneakyThrows
    private Object executeScriptFromFile(String file) {
        if (!(scriptEngine instanceof Invocable)) {
            throw new IllegalAccessException("Script engine does not support function calls");
        }

        String scriptContent = FileUtils.readFileToString(new File(file), StandardCharsets.UTF_8);
        ScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
        scriptEngine.eval(scriptContent, scriptContext);
        scriptEngine.setContext(scriptContext);

        Invocable invocable = (Invocable) scriptEngine;
        return invocable.invokeFunction("needToTrigger", dataHistory);
    }
}
