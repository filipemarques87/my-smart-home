package io.mysmarthome.service.impl;

import io.mysmarthome.device.Device;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.platform.message.ReceivedMessage;
import io.mysmarthome.service.DeviceManager;
import io.mysmarthome.service.DeviceSender;
import io.mysmarthome.service.MyPluginManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class DeviceSenderImpl implements DeviceSender {

    private final DeviceManager deviceManager;
    private final MyPluginManager<PlatformPlugin<? extends Device>> platformManager;

    @Override
    public CompletableFuture<Optional<ReceivedMessage>> send(String deviceId, Object payload) {
        DeviceEntity device = deviceManager.getDevice(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device id not " + deviceId + "found"));

        return platformManager.get(device.getPlatform())
                .send(device, payload);
    }
}
