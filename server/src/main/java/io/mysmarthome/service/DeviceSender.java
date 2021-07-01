package io.mysmarthome.service;

import io.mysmarthome.platform.ReceivedMessage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface DeviceSender {

    CompletableFuture<Optional<ReceivedMessage>> send(String deviceId, Object payload);
}
