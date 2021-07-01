package io.mysmarthome.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeviceGroupDto {
    String id;
    String name;
}
