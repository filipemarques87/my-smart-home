package io.mysmarthome.service.impl;

import io.mysmarthome.device.Device;
import io.mysmarthome.model.SendOnConditionTrigger;
import io.mysmarthome.model.entity.DeviceDataEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.SendOnConditionEntity;
import io.mysmarthome.platform.DownloadDetails;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.platform.message.ReceivedMessage;
import io.mysmarthome.service.DeviceInteraction;
import io.mysmarthome.service.DeviceManager;
import io.mysmarthome.service.MyPluginManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceInteractionImpl implements DeviceInteraction {

    private final DeviceManager deviceManager;
    private final MyPluginManager<PlatformPlugin<? extends Device>> platformManager;
    private final ScriptEngine scriptEngine;

    @Override
    public CompletableFuture<Optional<ReceivedMessage>> send(String deviceId, Object payload, SendOnConditionTrigger trigger) {
        log.info("Sending '{}' to device '{}'", payload, deviceId);

        try {
            DeviceEntity device = getDevice(deviceId);

            if (canSendToDevice(device, trigger)) {
                return platformManager.get(device.getPlatform())
                        .send(device, payload);
            }
            log.info("Not send to device because trigger was not satisfy");
            DeviceDataEntity o = deviceManager.getDeviceData(device.getDeviceId());
            return CompletableFuture.supplyAsync(() -> Optional.of(ReceivedMessage.builder()
                    .message(o != null ? o.getData() : null)
                    .build()));
        } catch (Exception ex) {
            log.error("Error on sending data to '{}'", deviceId, ex);
            throw ex;
        }
    }

    @Override
    public DownloadDetails download(String deviceId, String path) {
        log.info("Downloading '{}' from device '{}'", path, deviceId);

        try {
            DeviceEntity device = getDevice(deviceId);
            return platformManager.get(device.getPlatform())
                    .download(device, path);
        } catch (Exception ex) {
            log.error("Error on downloading from '{}'", deviceId, ex);
            throw ex;
        }
    }

    private DeviceEntity getDevice(String deviceId) {
        return deviceManager.getDevice(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device id not " + deviceId + "found"));
    }

    private boolean canSendToDevice(DeviceEntity device, SendOnConditionTrigger trigger) {
        String condition = device.getSendOnCondition().stream()
                .filter(s -> s.getTriggers().contains(SendOnConditionTrigger.ALL) || s.getTriggers().contains(trigger))
                .map(SendOnConditionEntity::getCondition)
                .findFirst()
                .orElse("true");

        ScriptExecutor scriptExecutor = new ScriptExecutor(scriptEngine);
        Object output = scriptExecutor.execute(condition);
        if (!(output instanceof Boolean)) {
            throw new NotificationException("Notification output must be a boolean value");
        }

        return (boolean) output;
    }
}
