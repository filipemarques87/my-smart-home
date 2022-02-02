package io.mysmarthome.service;

import io.mysmarthome.device.Device;
import io.mysmarthome.model.entity.ActionEntity;
import io.mysmarthome.model.entity.DeviceDataEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.NotificationEntity;
import io.mysmarthome.platform.message.OnReceive;
import io.mysmarthome.platform.message.ReceivedMessage;
import io.mysmarthome.service.impl.NotificationException;
import io.mysmarthome.service.impl.ScriptExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReceiveMessage implements OnReceive {

    private final DeviceInteraction deviceInteraction;
    private final DeviceManager deviceManager;
    private final ScriptEngine scriptEngine;
    private final NotificationService notifierService;

    @Override
    public void onReceive(Device device, ReceivedMessage msg) {
        log.info("Receive data for {}: {}", device.getDeviceId(), msg.getMessage().toString());

        try {
            deviceManager.saveData(device.getDeviceId(), msg.getReceivedAt(), msg.getMessage());

            List<Object> dataHistory = deviceManager.getDeviceData(device.getDeviceId(), 10).stream()
                    .map(DeviceDataEntity::getData)
                    .collect(Collectors.toList());

            ScriptExecutor scriptExecutor = new ScriptExecutor(dataHistory, scriptEngine);
            deviceManager.getDevice(device.getDeviceId())
                    .ifPresent(d -> {
                        notify(d, scriptExecutor);
                        triggerAction(d, scriptExecutor);
                    });
        } catch (Exception ex) {
            log.error("Error on receiving data from {}", device.getDeviceId(), ex);
        }
    }

    private void notify(DeviceEntity device, ScriptExecutor scriptExecutor) {
        device.getNotifications().stream()
                .filter(n -> needToNotify(scriptExecutor, n))
                .forEach(n -> notify(device, scriptExecutor, n));
    }

    private void triggerAction(DeviceEntity device, ScriptExecutor scriptExecutor) {
        device.getActions().stream()
                .filter(a -> needToPerformAction(scriptExecutor, a))
                .forEach(this::performAction);
    }

    private void performAction(ActionEntity action) {
        log.info("Action triggered for '{}'. Sending action to '{}'", action.getDeviceEntity().getDeviceId(), action.getTargetId());
        deviceManager.getDevice(action.getTargetId())
                .ifPresentOrElse(
                        d -> deviceInteraction.send(d.getDeviceId(), action.getPayload()),
                        () -> log.warn("Device id '{}' not found", action.getTargetId()));
    }

    private boolean needToPerformAction(ScriptExecutor scriptExecutor, ActionEntity action) {
        Object output = scriptExecutor.execute(action.getTrigger());
        if (!(output instanceof Boolean)) {
            throw new NotificationException("Notification output must be a boolean value");
        }
        return Boolean.TRUE.equals(output);
    }

    private boolean needToNotify(ScriptExecutor scriptExecutor, NotificationEntity notification) {
        Object output = scriptExecutor.execute(notification.getCondition());
        if (!(output instanceof Boolean)) {
            throw new NotificationException("Notification output must be a boolean value");
        }
        return Boolean.TRUE.equals(output);
    }

    private void notify(DeviceEntity deviceEntity, ScriptExecutor scriptExecutor, NotificationEntity notification) {
        log.info("Send notification for device '{}'", notification.getDeviceEntity().getDeviceId());
        String msg = Objects.toString(scriptExecutor.execute(notification.getMessage()));
        notifierService.notifyToAll(deviceEntity.getName(), msg);
    }
}
