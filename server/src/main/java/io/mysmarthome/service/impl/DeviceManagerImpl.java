package io.mysmarthome.service.impl;

import io.mysmarthome.model.Serializer;
import io.mysmarthome.model.entity.DeviceDataEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.DeviceGroupEntity;
import io.mysmarthome.repository.DeviceDataRepository;
import io.mysmarthome.repository.DeviceEntityRepository;
import io.mysmarthome.service.DeviceManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceManagerImpl implements DeviceManager {

    private final DeviceEntityRepository deviceEntityRepository;
    private final DeviceDataRepository deviceDataRepository;
    private final Serializer serializer;

    public DeviceEntity upsertDevice(DeviceEntity device) {
        Objects.requireNonNull(device, "Device must be not null");

        deviceEntityRepository.findById(device.getDeviceId())
                .ifPresentOrElse(
                        d -> log.info("Update device. Old state: {}, new state {}", d, device),
                        () -> log.info("Add new device {}", device));
        return deviceEntityRepository.save(device);
    }

    public Optional<DeviceEntity> getDevice(String deviceId) {
        return deviceEntityRepository.findById(deviceId);
    }

    public List<DeviceGroupEntity> getGroups() {
        return StreamSupport.stream(deviceEntityRepository.findAll().spliterator(), false)
                .map(DeviceEntity::getGroup)
                .filter(distinctByKey(DeviceGroupEntity::getGroupId))
                .collect(Collectors.toList());
    }

    public List<DeviceEntity> getDevicesForGroup(String groupId) {
        Objects.requireNonNull(groupId, "Group must be not null");
        return deviceEntityRepository.findByGroup_groupId(groupId);
    }

    public void saveData(String deviceId, Date receivedAt, Object message) {
        deviceDataRepository.save(DeviceDataEntity.builder()
                .deviceId(deviceId)
                .eventTime(receivedAt)
                .serializedData(serializer.serialize(message))
                .type(message.getClass().getCanonicalName())
                .build());
    }

    public DeviceDataEntity findDeviceData(String deviceId) {
        return deviceDataRepository.findById(deviceId)
                .map(d -> {
                    try {
                        d.setData(serializer.deserialize(d.getSerializedData(), Class.forName(d.getType())));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    return d;
                })
                .orElse(null);
    }

    @Override
    public void cleanDeviceDatabase() {
        deviceEntityRepository.deleteAll();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
