package io.mysmarthome.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Slf4j
@Configuration
public class ScriptEngineConfig {

    @Bean
    public ScriptEngine scriptEngine() {
        log.info("Loading the script engine");
        ScriptEngineManager manager = new ScriptEngineManager();
        return manager.getEngineByName("JavaScript");
    }
}
