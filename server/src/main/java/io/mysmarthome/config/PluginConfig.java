package io.mysmarthome.config;

import io.mysmarthome.configuration.ApplicationProperties;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.service.MyPluginManager;
import io.mysmarthome.service.impl.MyPluginManagerImpl;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class PluginConfig {

    @Bean
    public SpringPluginManager pluginManager() {
        return new SpringPluginManager();
    }

    @Bean
    public MyPluginManager<PlatformPlugin> platformManager(SpringPluginManager pluginManager, ApplicationProperties applicationProperties) {
        log.info("Start platforms plugins ...");
        List<PlatformPlugin> plugins = pluginManager.getExtensions(PlatformPlugin.class);

        Set<PlatformPlugin> pluginsToRemove = new HashSet<>();
        for (PlatformPlugin plugin : plugins) {
            try {
                plugin.start(applicationProperties);
            } catch (Exception ex) {
                pluginsToRemove.add(plugin);
                log.warn("*** Skip plugin {} ***", plugin.getName(), ex);
            }
        }

        log.info("... platforms plugins started");
        return new MyPluginManagerImpl<>(
                plugins.stream()
                        .filter(p -> !pluginsToRemove.contains(p))
                        .collect(Collectors.toList()));
    }
}
