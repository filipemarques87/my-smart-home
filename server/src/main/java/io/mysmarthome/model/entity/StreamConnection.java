package io.mysmarthome.model.entity;

import io.mysmarthome.repository.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class StreamConnection implements Identifiable {
    private String sessionId;
    private Set<DeviceConnection> connectedDevices;

    @Override
    public String getId() {
        return sessionId;
    }
}
