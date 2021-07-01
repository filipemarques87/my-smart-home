package io.mysmarthome.config;

import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.service.MyPluginManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppShutdown {

    private final SpringPluginManager pluginManager;
    private final MyPluginManager<PlatformPlugin> platformManager;

    @PreDestroy
    public void destroy() {
        log.info("Shutdown plugins ...");
        platformManager.shutdown();
        pluginManager.stopPlugins();
        pluginManager.unloadPlugins();
    }
}
