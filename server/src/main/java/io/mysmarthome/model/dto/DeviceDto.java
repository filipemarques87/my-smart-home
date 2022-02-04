package io.mysmarthome.model.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class DeviceDto {

    String deviceId;

    String name;

    String type;

    Object data;

    String lastUpdate;

    Map<String, Object> additionalInfo;
}
