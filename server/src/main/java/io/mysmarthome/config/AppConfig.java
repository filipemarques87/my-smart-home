package io.mysmarthome.config;

import io.mysmarthome.AppProperties;
import io.mysmarthome.configuration.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppConfig {

    @Bean
    public ApplicationProperties applicationProperties(AppProperties appProperties) {
        String configFile = appProperties.getConfigFile();
        log.info("Load '{}' application configuration file ", configFile);
        return new ApplicationProperties(configFile);
    }
}
