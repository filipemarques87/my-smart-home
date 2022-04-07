package io.mysmarthome.service;

import io.mysmarthome.model.entity.DeviceConnection;

import java.util.Set;

public interface StreamConnectionService {

    void addConnection(String sessionId, String subscriptionId, String deviceId);

    void removeConnection(String sessionId, String subscriptionId);

    Set<DeviceConnection> getConnections(String sessionId);
}
