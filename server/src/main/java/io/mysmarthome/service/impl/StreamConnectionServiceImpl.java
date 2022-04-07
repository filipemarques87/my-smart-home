package io.mysmarthome.service.impl;

import io.mysmarthome.model.entity.DeviceConnection;
import io.mysmarthome.model.entity.StreamConnection;
import io.mysmarthome.repository.StreamConnectionRepository;
import io.mysmarthome.service.StreamConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StreamConnectionServiceImpl implements StreamConnectionService {

    private final StreamConnectionRepository streamConnectionRepository;

    @Override
    public void addConnection(String sessionId, String subscriptionId, String deviceId) {
        Optional<StreamConnection> streamConnectionOpt = streamConnectionRepository.findById(sessionId);
        boolean alreadyConnected = streamConnectionOpt
                .map(StreamConnection::getConnectedDevices).stream()
                .flatMap(Collection::stream)
                .anyMatch(d -> d.getDeviceId().equals(deviceId));

        if (alreadyConnected) {
            // nothing to do
            return;
        }
        StreamConnection streamConnection;
        if (streamConnectionOpt.isEmpty()) {
            streamConnection = StreamConnection.builder()
                    .sessionId(sessionId)
                    .connectedDevices(new HashSet<>(List.of(new DeviceConnection(subscriptionId, deviceId))))
                    .build();
        } else {
            streamConnection = streamConnectionOpt.get();
            streamConnection.getConnectedDevices().add(new DeviceConnection(subscriptionId, deviceId));
        }
        streamConnectionRepository.save(streamConnection);
    }

    @Override
    public void removeConnection(String sessionId, String subscriptionId) {
        streamConnectionRepository.findById(sessionId)
                .ifPresent(s -> {
                            s.getConnectedDevices().stream()
                                    .filter(c -> c.getSubscriptionId().equals(subscriptionId))
                                    .findFirst()
                                    .ifPresent(dc -> s.getConnectedDevices().remove(dc));

                            if (s.getConnectedDevices().isEmpty()) {
                                streamConnectionRepository.delete(s);
                            }
                        }
                );
    }

    @Override
    public Set<DeviceConnection> getConnections(String sessionId) {
        return streamConnectionRepository.findById(sessionId)
                .map(StreamConnection::getConnectedDevices)
                .orElse(new HashSet<>());
    }
}
