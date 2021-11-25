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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

        device.getActions().forEach(a -> {
            Object p = Objects.requireNonNullElse(a.getPayload(), "");
            a.setSerializedPayload(serializer.serialize(p));
            a.setType(p.getClass().getCanonicalName());
        });

        device.getSchedulers().forEach(s -> {
            Object p = Objects.requireNonNullElse(s.getPayload(), "");
            s.setSerializedPayload(serializer.serialize(p));
            s.setType(p.getClass().getCanonicalName());
        });

        return deviceEntityRepository.save(device);
    }

    public Optional<DeviceEntity> getDevice(String deviceId) {
        Optional<DeviceEntity> optDeviceEntity = deviceEntityRepository.findById(deviceId);
        if (optDeviceEntity.isPresent()) {
            DeviceEntity deviceEntity = optDeviceEntity.get();
            deviceEntity.getActions().forEach(a -> {
                try {
                    a.setPayload(serializer.deserialize(a.getSerializedPayload(), Class.forName(a.getType())));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            deviceEntity.getSchedulers().forEach(s -> {
                try {
                    s.setPayload(serializer.deserialize(s.getSerializedPayload(), Class.forName(s.getType())));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            return Optional.of(deviceEntity);
        }
        return Optional.empty();
    }

    public List<DeviceGroupEntity> getGroups() {
        return StreamSupport.stream(deviceEntityRepository.findAll().spliterator(), false)
                .map(DeviceEntity::getGroup)
                .filter(distinctByKey(DeviceGroupEntity::getGroupId))
                .collect(Collectors.toList());
    }

    public List<DeviceEntity> getDevicesForGroup(String groupId) {
        Objects.requireNonNull(groupId, "Group must be not null");
        return deviceEntityRepository.findByGroup_groupId(groupId).stream()
                .peek(d -> {
                    d.getActions().forEach(a -> {
                        try {
                            a.setPayload(serializer.deserialize(a.getSerializedPayload(), Class.forName(a.getType())));
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    d.getSchedulers().forEach(s -> {
                        try {
                            s.setPayload(serializer.deserialize(s.getSerializedPayload(), Class.forName(s.getType())));
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                })
                .collect(Collectors.toList());
    }

    public void saveData(String deviceId, Instant receivedAt, Object message) {
        deviceDataRepository.save(DeviceDataEntity.builder()
                .deviceId(deviceId)
                .eventTime(receivedAt)
                .serializedData(serializer.serialize(message))
                .type(message.getClass().getCanonicalName())
                .build());
    }

    public List<DeviceDataEntity> getDeviceData(String deviceId, int limit) {
        Pageable sortedByPriceDesc = PageRequest.of(0, limit, Sort.by("eventTime").descending());
        return deviceDataRepository.findAllByDeviceId(deviceId, sortedByPriceDesc).stream()
                .map(d -> {
                    try {
                        d.setData(serializer.deserialize(d.getSerializedData(), Class.forName(d.getType())));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    return d;
                })
                .collect(Collectors.toList());
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
