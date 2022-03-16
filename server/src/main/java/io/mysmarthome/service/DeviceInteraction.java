package io.mysmarthome.service;

import io.mysmarthome.model.SendOnConditionTrigger;
import io.mysmarthome.platform.DownloadDetails;
import io.mysmarthome.platform.message.ReceivedMessage;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface DeviceInteraction {

    CompletableFuture<Optional<ReceivedMessage>> send(String deviceId, Object payload, SendOnConditionTrigger trigger);

    DownloadDetails download(String deviceId, String path);
}
