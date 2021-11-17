package io.mysmarthome.platform;

import io.mysmarthome.BasicPlugin;
import io.mysmarthome.device.Device;
import io.mysmarthome.platform.message.OnReceive;
import io.mysmarthome.platform.message.ReceivedMessage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PlatformPlugin<T extends Device> extends BasicPlugin {
    default void registerDevice(Device device, OnReceive callback) {
        onRegisterDevice(getPlatformSpecificDevice(device), callback);
    }

    void onRegisterDevice(T device, OnReceive callback);

    T getPlatformSpecificDevice(Device device);

    default CompletableFuture<Optional<ReceivedMessage>> send(Device device, Object payload) {
        return onSend(getPlatformSpecificDevice(device), payload);
    }

    CompletableFuture<Optional<ReceivedMessage>> onSend(T device, Object payload);
}
