package io.mysmarthome.config;

import io.mysmarthome.configuration.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppConfig {
    @Bean
    public ApplicationProperties applicationProperties() {
        String filename = SystemProperty.CONFIGURATION_FILE.getValue();
        log.info("Load '{}' application configuration file ", filename);
        return new ApplicationProperties(filename);
    }
}
