package io.mysmarthome.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeviceDto {

    String deviceId;

    String name;

    String type;

    String units;

    Object data;

    String lastUpdate;
}
