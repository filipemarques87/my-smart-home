package io.mysmarthome.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.Set;

public interface StreamConnectionService {

    void addConnection(WebSocketSession session, String deviceId);

    Optional<WebSocketSession> getConnection(WebSocketSession session);

    void removeConnection(WebSocketSession session, String deviceId);

    Set<String> getDevices(WebSocketSession session);

    Set<WebSocketSession> getSessionsByDeviceId(String deviceId);
}
