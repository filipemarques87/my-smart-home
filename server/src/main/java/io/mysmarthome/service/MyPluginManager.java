package io.mysmarthome.service;

import io.mysmarthome.BasicPlugin;

public interface MyPluginManager<T extends BasicPlugin> {

    boolean exists(String type);

    T get(String type);

    void shutdown();
}
