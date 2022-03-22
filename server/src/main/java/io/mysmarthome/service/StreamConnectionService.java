package io.mysmarthome.service;

import java.util.Set;

public interface StreamConnectionService {

    void addConnection(String sessionId, String deviceId);

    void removeConnection(String sessionId, String deviceId);

    Set<String> getConnections(String sessionId);
}
