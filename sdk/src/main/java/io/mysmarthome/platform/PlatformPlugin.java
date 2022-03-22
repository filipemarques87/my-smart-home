package io.mysmarthome.platform;

import io.mysmarthome.BasicPlugin;
import io.mysmarthome.device.Device;
import io.mysmarthome.platform.message.OnReceive;
import io.mysmarthome.platform.message.ReceivedMessage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface PlatformPlugin<T extends Device> extends BasicPlugin {
    default void registerDevice(Device device, OnReceive callback) {
        onRegisterDevice(getPlatformSpecificDevice(device), callback);
    }

    void onRegisterDevice(T device, OnReceive callback);

    T getPlatformSpecificDevice(Device device);

    default CompletableFuture<Optional<ReceivedMessage>> send(Device device, Object payload) {
        return onSend(getPlatformSpecificDevice(device), payload);
    }

    default CompletableFuture<Optional<ReceivedMessage>> onSend(T device, Object payload) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    default DownloadDetails download(Device device, String path) {
        return onDownload(getPlatformSpecificDevice(device), path);
    }

    default DownloadDetails onDownload(T device, String path) {
        return null;
    }

    default void startStream(Device device, Consumer<Object> processPayload) {
        onStartStream(getPlatformSpecificDevice(device), processPayload);
    }

    default void onStartStream(T device, Consumer<Object> processPayload) {
    }

    default void stopStream(Device device) {
        onStopStream(getPlatformSpecificDevice(device));
    }

    default void onStopStream(T device) {
    }
}
