package io.mysmarthome.model.entity;

import io.mysmarthome.repository.Identifiable;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Data
@Builder
public class StreamConnection implements Identifiable {
    private String sessionId;
    private WebSocketSession session;
    private Set<String> devices;

    @Override
    public String getId() {
        return sessionId;
    }
}
