package io.mysmarthome.service.impl;

import io.mysmarthome.model.entity.StreamConnection;
import io.mysmarthome.repository.StreamConnectionRepository;
import io.mysmarthome.service.DeviceInteraction;
import io.mysmarthome.service.StreamConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StreamConnectionServiceImpl implements StreamConnectionService {

    private final StreamConnectionRepository streamConnectionRepository;
    private final DeviceInteraction deviceInteraction;

    @Override
    public void addConnection(String sessionId, String deviceId) {
        Optional<StreamConnection> streamConnectionOpt = streamConnectionRepository.findById(sessionId);
        boolean alreadyConnected = streamConnectionOpt
                .map(StreamConnection::getConnectedDevices).stream()
                .flatMap(Collection::stream)
                .anyMatch(d -> d.equals(deviceId));

        if (alreadyConnected) {
            // nothing to do
            return;
        }
        StreamConnection streamConnection;
        if (streamConnectionOpt.isEmpty()) {
            streamConnection = StreamConnection.builder()
                    .sessionId(sessionId)
                    .connectedDevices(new HashSet<>(List.of(deviceId)))
                    .build();
        } else {
            streamConnection = streamConnectionOpt.get();
            streamConnection.getConnectedDevices().add(deviceId);
        }
        streamConnectionRepository.save(streamConnection);
    }

    @Override
    public void removeConnection(String sessionId, String deviceId) {
        streamConnectionRepository.findById(sessionId)
                .ifPresent(streamConnectionRepository::delete);
    }

    @Override
    public Set<String> getConnections(String sessionId) {
        return streamConnectionRepository.findById(sessionId)
                .map(StreamConnection::getConnectedDevices)
                .orElse(new HashSet<>());
    }
}
