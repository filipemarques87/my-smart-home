package io.mysmarthome.api.websocket;

import io.mysmarthome.service.DeviceInteraction;
import io.mysmarthome.service.StreamConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.net.URI;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static io.mysmarthome.util.SneakyException.sneakyException;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeviceWebSocketHandler extends AbstractWebSocketHandler {

    public static final String BASE_URI = "/device";

    private final DeviceInteraction deviceInteraction;
    private final StreamConnectionService streamConnectionService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        String[] pathParts = extractUriPathParts(session.getUri());
        String messageType = pathParts[0];
        String deviceId = pathParts[1];

        streamConnectionService.addConnection(new WebSocketSessionUDP(session), deviceId);
        try {
            deviceInteraction.startStream(deviceId, sessionId, data -> {
                WebSocketMessage<?> message = getWebSocketMessage(messageType).apply(data);
                // broadcast message for all sessions connected to deviceId
                streamConnectionService.getSessionsByDeviceId(deviceId)
                        .forEach(sneakyException(s -> {
                            s.sendMessage(message);
                        }));
            });
        } catch (Exception e) {
            log.error("Error while starting stream for device {}", deviceId, e);
            streamConnectionService.removeConnection(session, deviceId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();

        Set<String> devices = streamConnectionService.getDevices(session);
        devices.forEach(deviceId -> {
            streamConnectionService.removeConnection(session, deviceId);
            try {
                deviceInteraction.stopStream(deviceId, sessionId);
            } catch (Exception e) {
                log.error("Error while stopping stream for device {}", deviceId, e);
                streamConnectionService.removeConnection(session, deviceId);
            }
        });
    }

    private String[] extractUriPathParts(URI uri) {
        String[] parts = Optional.ofNullable(uri)
                .map(URI::getPath)
                .filter(p -> p.startsWith(BASE_URI))
                .map(p -> p.split("/"))
                .orElseThrow();

        return new String[]{parts[2], parts[3]};
    }

    private Function<Object, WebSocketMessage<?>> getWebSocketMessage(String messageType) {
        switch (messageType) {
            case "text":
                return d -> {
                    if (!(d instanceof String)) {
                        throw new IllegalArgumentException("Expected " + String.class.getCanonicalName() + " but got " + (d == null ? "null" : d.getClass().getCanonicalName()));
                    }
                    return new TextMessage((String) d);
                };
            case "binary":
                return d -> {
                    if (!(d instanceof byte[])) {
                        throw new IllegalArgumentException("Expected " + byte[].class.getCanonicalName() + " but got " + (d == null ? "null" : d.getClass().getCanonicalName()));
                    }
                    return new BinaryMessage((byte[]) d);
                };
            default:
                throw new UnsupportedOperationException("Message type " + messageType + " not supported");
        }
    }
}
