package io.mysmarthome.service.impl;

import io.mysmarthome.model.entity.StreamConnection;
import io.mysmarthome.repository.StreamConnectionRepository;
import io.mysmarthome.service.StreamConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StreamConnectionServiceImpl implements StreamConnectionService {

    private final StreamConnectionRepository streamConnectionRepository;

    @Override
    public void addConnection(WebSocketSession session, String deviceId) {
        String sessionId = session.getId();
        Optional<StreamConnection> streamConnectionOpt = streamConnectionRepository.findById(sessionId);
        boolean alreadyConnected = streamConnectionOpt
                .map(StreamConnection::getDevices).stream()
                .flatMap(Collection::stream)
                .anyMatch(deviceId::equals);

        if (alreadyConnected) {
            // nothing to do
            return;
        }
        StreamConnection streamConnection;
        if (streamConnectionOpt.isEmpty()) {
            streamConnection = StreamConnection.builder()
                    .sessionId(sessionId)
                    .session(session)
                    .devices(new HashSet<>(List.of(deviceId)))
                    .build();
        } else {
            streamConnection = streamConnectionOpt.get();
            streamConnection.getDevices().add(deviceId);
        }
        streamConnectionRepository.save(streamConnection);
    }

    @Override
    public Optional<WebSocketSession> getConnection(WebSocketSession session) {
        return streamConnectionRepository.findById(session.getId())
                .map(StreamConnection::getSession);
    }

    @Override
    public void removeConnection(WebSocketSession session, String deviceId) {
        String sessionId = session.getId();
        streamConnectionRepository.findById(sessionId)
                .ifPresent(s -> {
                            s.getDevices().stream()
                                    .filter(deviceId::equals)
                                    .findFirst()
                                    .ifPresent(d -> s.getDevices().remove(d));

                            if (s.getDevices().isEmpty()) {
                                streamConnectionRepository.delete(s);
                            }
                        }
                );
    }

    @Override
    public Set<String> getDevices(WebSocketSession session) {
        return streamConnectionRepository.findById(session.getId())
                .map(StreamConnection::getDevices)
                .orElse(new HashSet<>());
    }

    @Override
    public Set<WebSocketSession> getSessionsByDeviceId(String deviceId) {
        return StreamSupport.stream(streamConnectionRepository.findAll().spliterator(), false)
                .filter(s -> s.getDevices().contains(deviceId))
                .map(StreamConnection::getSession)
                .collect(Collectors.toSet());
    }
}
