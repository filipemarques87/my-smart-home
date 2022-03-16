package io.mysmarthome.service.impl;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.script.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ScriptExecutor {

    private final ScriptEngine scriptEngine;
    private final Map<String, Object> data;

    public ScriptExecutor(ScriptEngine scriptEngine) {
        this(new HashMap<>(), scriptEngine);
    }

    public ScriptExecutor(Map<String, Object> customData, ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
        this.data = new HashMap<>();

        customData.forEach((k, v) -> data.merge(k, v, (ov, nv) -> nv));
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
        engineScope.put("$v", data);
        return scriptEngine.eval(script, scriptContext);
    }

    @SneakyThrows
    private Object executeScriptFromFile(String file) {
        if (!(scriptEngine instanceof Invocable)) {
            throw new IllegalAccessException("Script engine does not support function calls");
        }

        String filename = extractScriptFilename(file);
        String scriptContent = FileUtils.readFileToString(new File(filename), StandardCharsets.UTF_8);
        ScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
        scriptEngine.eval(scriptContent, scriptContext);
        scriptEngine.setContext(scriptContext);

        Invocable invocable = (Invocable) scriptEngine;
        String methodName = extractScriptMethod(file);
        return invocable.invokeFunction(methodName, data);
    }

    private String extractScriptFilename(String file) {
        return file.split(":")[0];
    }

    private String extractScriptMethod(String file) {
        String[] parts = file.split(":");
        if (parts.length == 1) {
            return "needToTrigger";
        }
        return parts[1];
    }
}
