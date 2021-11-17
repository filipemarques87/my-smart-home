package io.mysmarthome.service.impl;

import io.mysmarthome.BasicPlugin;
import io.mysmarthome.device.Device;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.service.MyPluginManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MyPluginManagerImpl<T extends PlatformPlugin<? extends Device>> implements MyPluginManager<T> {

    private final Map<String, T> plugins;

    public MyPluginManagerImpl(List<T> plugins) {
        this.plugins = Collections.unmodifiableMap(plugins.stream()
                .collect(Collectors.toMap(BasicPlugin::getName, p -> p)));
    }

    public boolean exists(String type) {
        return plugins.containsKey(type);
    }

    public T get(String type) {
        if (plugins.containsKey(type)) {
            return plugins.get(type);
        }
        throw new IllegalArgumentException("Plugin of type '" + type + "' does not exist");
    }

    public void shutdown() {
        plugins.values().forEach(p -> {
            try {
                p.shutdown();
            } catch (Exception e) {
                log.warn("Error while shutdown plugin {}", p.getName(), e);
            }
        });
    }
}
