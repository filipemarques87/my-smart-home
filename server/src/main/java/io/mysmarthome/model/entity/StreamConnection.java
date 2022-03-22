package io.mysmarthome.model.entity;

import io.mysmarthome.repository.Identifiable;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class StreamConnection implements Identifiable {
    private String sessionId;
    private Set<String> connectedDevices;

    @Override
    public String getId() {
        return sessionId;
    }
}
