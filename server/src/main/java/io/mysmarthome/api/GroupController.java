package io.mysmarthome.api;

import io.mysmarthome.model.dto.DeviceDto;
import io.mysmarthome.model.dto.DeviceGroupDto;
import io.mysmarthome.model.mapper.DeviceGroupMapper;
import io.mysmarthome.model.mapper.DeviceMapper;
import io.mysmarthome.service.DeviceManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final DeviceManager deviceManager;
    private final DeviceGroupMapper deviceGroupMapper;
    private final DeviceMapper deviceMapper;

    @GetMapping("")
    public List<DeviceGroupDto> getGroups() {
        log.info("Get devices groups");
        return deviceManager.getGroups().stream()
                .map(deviceGroupMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{groupId}/devices")
    public List<DeviceDto> getDevicesByGroup(@PathVariable("groupId") String groupId) {
        log.info("Get devices for groupId: {}", groupId);
        return deviceManager.getDevicesForGroup(groupId).stream()
                .map(d -> deviceMapper.toDto(d, deviceManager.findDeviceData(d.getDeviceId())))
                .collect(Collectors.toList());
    }
}

