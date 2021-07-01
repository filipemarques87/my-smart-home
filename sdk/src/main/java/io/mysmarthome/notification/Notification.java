package io.mysmarthome.notification;

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.*;
//import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.Objects;
import java.util.function.Supplier;

//@Slf4j
//@Data
//@Setter(AccessLevel.NONE)
//@NoArgsConstructor
//@AllArgsConstructor
public interface Notification {
//    @JsonProperty("trigger")
//    @Getter(AccessLevel.NONE)
//    private String trigger;
//
//    @JsonProperty("message")
//    @Getter(AccessLevel.NONE)
//    private String message;
//
//    @JsonIgnore
//    @Getter(AccessLevel.NONE)
//    private transient ScriptEngine engine;
//
//    @JsonIgnore
//    @Getter(AccessLevel.NONE)
//    private transient ScriptContext scriptContext;
//
//    @Getter(AccessLevel.NONE)
//    private Supplier<String> lastMessage;

    void addScriptEngine(ScriptEngine engine);
//    public void addScriptEngine(ScriptEngine engine) {
//        this.engine = engine;
//        scriptContext = new SimpleScriptContext();
//        scriptContext.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
//    }

    boolean needToNotify(Object val);
//    public boolean needToNotify(Object val) {
//        try {
//            Bindings engineScope = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
//            engineScope.put("$v", val);
//
//            log.debug("Compute trigger: {}, {}", trigger, val);
//            Object output = engine.eval(trigger, scriptContext);
//            if (!(output instanceof Boolean)) {
//                throw new NotificationException("Notification output must be a boolean value");
//            }
//            boolean toNotify = Boolean.TRUE.equals(output);
//            log.debug("Notification value: {}", toNotify);
//            if (toNotify) {
//                lastMessage = () -> {
//                    try {
//                        return Objects.toString(engine.eval(message, scriptContext));
//                    } catch (ScriptException e) {
//                        throw new NotificationException("Exception on compile message",  e);
//                    }
//                };
//            }
//            return toNotify;
//        } catch (ScriptException e) {
//            throw new NotificationException("Exception on compute the trigger", e);
//        }
//    }

    String getLastMessage();
//    public String getLastMessage() {
//        if (lastMessage != null) {
//            String compiledMessage = lastMessage.get();
//            lastMessage = null;
//            return compiledMessage;
//        }
//        throw new NotificationException("No message available");
//    }
}
