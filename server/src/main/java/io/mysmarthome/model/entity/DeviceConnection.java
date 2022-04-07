package io.mysmarthome.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DeviceConnection {
    private String subscriptionId;
    private String deviceId;
}