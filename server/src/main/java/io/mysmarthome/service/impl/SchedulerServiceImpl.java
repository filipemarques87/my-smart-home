package io.mysmarthome.service.impl;

import io.mysmarthome.device.Device;
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
                        .ifPresent(s -> {
                            log.info("Sending '{}' to device '{}'", s.getPayload(), d.getDeviceId());
                            platformManager.get(d.getPlatform())
                                    .send(d, s.getPayload());
                            log.info("Data sent to device '{}'", d.getDeviceId());
                        }));
    }
}
