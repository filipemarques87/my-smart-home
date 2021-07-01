package io.mysmarthome.platform;

import io.mysmarthome.BasicPlugin;
import io.mysmarthome.device.Device;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PlatformPlugin extends BasicPlugin {
    default void registerDevice(Device device, OnReceive callback) {
    }

    CompletableFuture<Optional<ReceivedMessage>> send(Device device, Object payload);
}
