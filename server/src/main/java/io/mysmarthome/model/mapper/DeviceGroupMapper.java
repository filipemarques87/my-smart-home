package io.mysmarthome.model.mapper;

import io.mysmarthome.model.dto.DeviceGroupDto;
import io.mysmarthome.model.entity.DeviceGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceGroupMapper {

    @Mapping(source = "groupId", target = "id")
    DeviceGroupDto toDto(DeviceGroupEntity model);
}
