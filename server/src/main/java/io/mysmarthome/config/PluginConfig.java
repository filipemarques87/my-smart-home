package io.mysmarthome.config;

import io.mysmarthome.AppConstants;
import io.mysmarthome.configuration.ApplicationProperties;
import io.mysmarthome.device.Device;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.service.MyPluginManager;
import io.mysmarthome.service.impl.MyPluginManagerImpl;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.RuntimeMode;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class PluginConfig {

    static {
        System.setProperty("pf4j.mode", RuntimeMode.DEPLOYMENT.toString());
    }

    @Bean
    public SpringPluginManager pluginManager() {
        return new SpringPluginManager(Paths.get(AppConstants.PLATFORMS_FOLDER));
    }

    @Bean
    public MyPluginManager<PlatformPlugin<? extends Device>> platformManager(SpringPluginManager pluginManager, ApplicationProperties applicationProperties) {
        log.info("Start platforms plugins ...");
        List<?> pluginsTemp = pluginManager.getExtensions(PlatformPlugin.class);
        List<PlatformPlugin<? extends Device>> plugins = new ArrayList<>(pluginsTemp.size());
        for (Object o : pluginsTemp) {
            plugins.add((PlatformPlugin<? extends Device>) o);
        }

        Set<PlatformPlugin<? extends Device>> pluginsToRemove = new HashSet<>();
        for (PlatformPlugin<? extends Device> plugin : plugins) {
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
