package io.mysmarthome.service.impl;

import io.mysmarthome.model.SendOnConditionTrigger;
import io.mysmarthome.service.DeviceManager;
import io.mysmarthome.service.DeviceInteraction;
import io.mysmarthome.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final DeviceInteraction deviceInteraction;
    private final DeviceManager deviceManager;

    @Override
    public void schedulerTriggered(String deviceId, String schedulerId) {
        deviceManager.getDevice(deviceId)
                .flatMap(d -> d.getSchedulers().stream()
                        .filter(s -> s.getId().equals(schedulerId))
                        .findAny())
                .ifPresent(s -> deviceInteraction.send(deviceId, s.getPayload(), SendOnConditionTrigger.SCHEDULER));
    }
}
