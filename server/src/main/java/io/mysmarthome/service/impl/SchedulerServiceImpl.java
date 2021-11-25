package io.mysmarthome.service.impl;

import io.mysmarthome.device.Device;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.SchedulerEntity;
import io.mysmarthome.platform.PlatformPlugin;
import io.mysmarthome.service.DeviceManager;
import io.mysmarthome.service.MyPluginManager;
import io.mysmarthome.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final DeviceManager deviceManager;
    private final MyPluginManager<? extends PlatformPlugin<? extends Device>> platformManager;

    @Override
    public void schedulerTriggered(String deviceId, String schedulerId) {
        deviceManager.getDevice(deviceId)
                .ifPresent(d -> d.getSchedulers().stream()
                        .filter(s -> s.getId().equals(schedulerId))
                        .findAny()
                        .ifPresent(s -> sendToDeviceOnScheduler(d, s)));
    }

    private void sendToDeviceOnScheduler(DeviceEntity deviceEntity, SchedulerEntity schedulerEntity) {
        log.info("Sending '{}' to device '{}'", schedulerEntity.getPayload(), deviceEntity.getDeviceId());
        try {
            platformManager.get(deviceEntity.getPlatform())
                    .send(deviceEntity, schedulerEntity.getPayload());
            log.info("Data sent to device '{}'", deviceEntity.getDeviceId());
        } catch (Exception ex) {
            log.error("Error on sending data to '{}'", deviceEntity.getDeviceId(), ex);
        }
    }
}
