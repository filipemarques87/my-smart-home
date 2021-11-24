package io.mysmarthome.service;

import io.mysmarthome.device.Device;
import io.mysmarthome.model.entity.DeviceDataEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.DeviceGroupEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DeviceManager {

    DeviceEntity upsertDevice(DeviceEntity device);

    Optional<DeviceEntity> getDevice(String deviceId);

    List<DeviceGroupEntity> getGroups();

    List<DeviceEntity> getDevicesForGroup(String groupId);

    void saveData(String id, Date receivedAt, Object message);

    DeviceDataEntity getDeviceData(String deviceId);

    void cleanDeviceDatabase();
}
