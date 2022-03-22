package io.mysmarthome.service;

import io.mysmarthome.model.SendOnConditionTrigger;
import io.mysmarthome.platform.DownloadDetails;
import io.mysmarthome.platform.message.ReceivedMessage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface DeviceInteraction {

    CompletableFuture<Optional<ReceivedMessage>> send(String deviceId, Object payload, SendOnConditionTrigger trigger);

    DownloadDetails download(String deviceId, String path);

    void startStream(String deviceId, String sessionId, Consumer<Object> processPayload);

    void stopStream(String deviceId, String sessionId);
}
