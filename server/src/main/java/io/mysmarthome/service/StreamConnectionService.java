package io.mysmarthome.service;

import io.mysmarthome.model.entity.DeviceConnection;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public interface StreamConnectionService {

    void addConnection(WebSocketSession session, String deviceId);

    void removeConnection(WebSocketSession session, String deviceId);

    Set<String> getDevices(WebSocketSession session);

    Set<WebSocketSession> getSessionsByDeviceId(String deviceId);
}
