package io.mysmarthome.model.mapper;

import io.mysmarthome.model.config.ActionConfig;
import io.mysmarthome.model.config.DeviceConfig;
import io.mysmarthome.model.config.SchedulerConfig;
import io.mysmarthome.model.dto.DeviceDto;
import io.mysmarthome.model.entity.ActionEntity;
import io.mysmarthome.model.entity.DeviceDataEntity;
import io.mysmarthome.model.entity.DeviceEntity;
import io.mysmarthome.model.entity.SchedulerEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.io.ObjectInputFilter;
import java.text.Normalizer;
import java.util.Locale;

@Mapper(componentModel = "spring")
public abstract class DeviceMapper {

    public abstract SchedulerEntity fromConfig(SchedulerConfig schedulerConfig);

    @Mapping(target = "targetId", source = "deviceId")
    public abstract ActionEntity fromConfig(ActionConfig deviceConfig);

    @Mapping(target = "group.name", source = "group")
    public abstract DeviceEntity fromConfig(DeviceConfig deviceConfig);

    @Mapping(source = "device.deviceId", target = "deviceId")
    @Mapping(source = "device.name", target = "name")
    @Mapping(source = "device.type", target = "type")
    @Mapping(source = "device.units", target = "units")
    @Mapping(source = "deviceDataEntity.data", target = "data")
    @Mapping(source = "deviceDataEntity.eventTime", target = "lastUpdate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public abstract DeviceDto toDto(DeviceEntity device, DeviceDataEntity deviceDataEntity);

    @AfterMapping
    protected void postDeviceFromConfig(@MappingTarget DeviceEntity deviceEntity) {
        deviceEntity.getGroup().setDeviceEntity(deviceEntity);
        if (deviceEntity.getSchedulers() != null) {
            deviceEntity.getSchedulers().forEach(n -> n.setDeviceEntity(deviceEntity));
        }
        if (deviceEntity.getNotifications() != null) {
            deviceEntity.getNotifications().forEach(n -> n.setDeviceEntity(deviceEntity));
        }
        if (deviceEntity.getActions() != null) {
            deviceEntity.getActions().forEach(n -> n.setDeviceEntity(deviceEntity));
        }

        if (StringUtils.isBlank(deviceEntity.getDeviceId())) {
            deviceEntity.setDeviceId(
                    Normalizer.normalize(deviceEntity.getName(), Normalizer.Form.NFD)
                            .toLowerCase(Locale.ROOT)
                            .replaceAll("\\s+", "-"));
        }
    }
}
