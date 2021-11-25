package io.mysmarthome.service;

import io.mysmarthome.model.entity.DeviceDataEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.DeviceGroupEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DeviceManager {

    DeviceEntity upsertDevice(DeviceEntity device);

    Optional<DeviceEntity> getDevice(String deviceId);

    List<DeviceGroupEntity> getGroups();

    List<DeviceEntity> getDevicesForGroup(String groupId);

    void saveData(String id, Instant receivedAt, Object message);

    default DeviceDataEntity getDeviceData(String deviceId) {
        return getDeviceData(deviceId, 1).stream()
                .findFirst()
                .orElse(null);
    }

    List<DeviceDataEntity> getDeviceData(String deviceId, int limit);

    void cleanDeviceDatabase();
}
