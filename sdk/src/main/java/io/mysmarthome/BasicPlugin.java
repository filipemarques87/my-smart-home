package io.mysmarthome;

import io.mysmarthome.configuration.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.ExtensionPoint;

public interface BasicPlugin extends ExtensionPoint {
    default void start(ApplicationProperties config) {
    }

    default void shutdown() {
    }

    String getName();
}
