package io.mysmarthome.model.mapper;

import io.mysmarthome.model.dto.DeviceGroupDto;
import io.mysmarthome.model.dto.DeviceGroupDto.DeviceGroupDtoBuilder;
import io.mysmarthome.model.entity.DeviceGroupEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-07-01T05:07:48+0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Ubuntu)"
)
@Component
public class DeviceGroupMapperImpl implements DeviceGroupMapper {

    @Override
    public DeviceGroupDto toDto(DeviceGroupEntity model) {
        if ( model == null ) {
            return null;
        }

        DeviceGroupDtoBuilder deviceGroupDto = DeviceGroupDto.builder();

        deviceGroupDto.id( model.getGroupId() );
        deviceGroupDto.name( model.getName() );

        return deviceGroupDto.build();
    }
}
