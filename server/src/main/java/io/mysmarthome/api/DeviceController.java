package io.mysmarthome.api;

import io.mysmarthome.configuration.ApplicationProperties;
import io.mysmarthome.model.SendOnConditionTrigger;
import io.mysmarthome.model.entity.DeviceConnection;
import io.mysmarthome.platform.DownloadDetails;
import io.mysmarthome.platform.message.ReceivedMessage;
import io.mysmarthome.service.DeviceInteraction;
import io.mysmarthome.service.StreamConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceInteraction deviceInteraction;
    private final ApplicationProperties applicationProperties;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final StreamConnectionService streamConnectionService;

    @RequestMapping(value = "/{deviceId}/download/**", method = RequestMethod.GET)
    public void downloadFromDevice(@PathVariable("deviceId") String deviceId, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        log.info("Receive download request for device {}: {}", deviceId, request.getRequestURI());
        String filePath = request.getRequestURI().substring(("/device/" + deviceId + "/download").length());

        DownloadDetails downloadDetails = deviceInteraction.download(deviceId, filePath);
        response.setContentType(downloadDetails.getMimeType());
        response.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", downloadDetails.getFilename()));
        response.setContentLength((int) downloadDetails.getFileSize());
        InputStream inputStream = new BufferedInputStream(downloadDetails.getFileSstream());
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @PostMapping(value = "/{deviceId}")
    public Object sentToDevice(@PathVariable("deviceId") String deviceId, @RequestBody Map<String, Object> msg) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("Receive action request for device {}: {}", deviceId, msg);
        return deviceInteraction
                .send(deviceId, msg, SendOnConditionTrigger.MANUAL)
                .get(applicationProperties.getInt("api.timeout"), TimeUnit.SECONDS)
                .map(this::mapResponse)
                .orElseThrow(() -> new IllegalArgumentException("Not able to get response from " + deviceId));
    }

    private Map<String, Object> mapResponse(ReceivedMessage rm) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", rm.getMessage());
        response.put("lastUpdate", rm.getReceivedAt());
        return response;
    }

    @EventListener
    public void handleSubscribe(final SessionSubscribeEvent event) {
        Optional<String> sessionIdOpt = getSessionId(event);
        if (sessionIdOpt.isEmpty()) {
            log.error("Session id is not present in subscribe event");
            return;
        }

        Optional<String> subscriptionIdOpt = getSubscriptionId(event);
        if (subscriptionIdOpt.isEmpty()) {
            log.error("Subscription id is not present in subscribe event");
            return;
        }

        Optional<String> deviceIdOpt = getDeviceId(event);
        if (deviceIdOpt.isEmpty()) {
            log.error("Device id is not present in subscribe event");
            return;
        }

        String sessionId = sessionIdOpt.get();
        String subscriptionId = subscriptionIdOpt.get();
        String deviceId = deviceIdOpt.get();
        log.info("Add connection for sessionId [{}] and for deviceId [{}]", sessionId, deviceId);
        streamConnectionService.addConnection(sessionId, subscriptionId, deviceId);
        try {
            deviceInteraction.startStream(deviceId, sessionId,
                    data -> {
                        String dataToSend;
                        if (data instanceof byte[]) {
                            dataToSend = Base64.getEncoder().encodeToString((byte[]) data);
                        } else if (data == null) {
                            dataToSend = "";
                        } else {
                            throw new UnsupportedOperationException("Need to implement converter for " + data.getClass().getCanonicalName());
                        }
                        simpMessagingTemplate.convertAndSend("/topic/" + deviceId, dataToSend);
                    });
        } catch (Exception e) {
            log.error("Error while starting stream for device {}", deviceId, e);
            streamConnectionService.removeConnection(sessionId, deviceId);
        }
    }

    @EventListener
    public void handleUnsubscribe(final SessionUnsubscribeEvent event) {
        Optional<String> sessionIdOpt = getSessionId(event);
        if (sessionIdOpt.isEmpty()) {
            log.error("Session id is not present in subscribe event");
            return;
        }

//        Optional<String> deviceIdOpt = getDeviceId(event);
//        if (deviceIdOpt.isEmpty()) {
//            log.error("Device id is not present in subscribe event");
//            return;
//        }

        Optional<String> subscriptionIdOpt = getSubscriptionId(event);
        if (subscriptionIdOpt.isEmpty()) {
            log.error("Subscription id is not present in subscribe event");
            return;
        }

        String sessionId = sessionIdOpt.get();
        String subscriptionId = subscriptionIdOpt.get();

        Set<DeviceConnection> connections = streamConnectionService.getConnections(sessionId);
        Optional<String> deviceIdOpt = connections.stream()
                .filter(c -> c.getSubscriptionId().equals(subscriptionId))
                .map(DeviceConnection::getDeviceId)
                .findFirst();

        if (deviceIdOpt.isEmpty()) {
            log.error("Could not find a device id linked to subscription id {}", subscriptionId);
            return;
        }

        log.info("Remove connection for sessionId [{}] and for deviceId [{}]", sessionId, subscriptionId);
        streamConnectionService.removeConnection(sessionId, subscriptionId);

        String deviceId = deviceIdOpt.get();
        try {
            deviceInteraction.stopStream(deviceId, sessionId);
        } catch (Exception e) {
            log.error("Error while stopping stream for device {}", deviceId, e);
            streamConnectionService.removeConnection(sessionId, deviceId);
        }
    }

    @EventListener
    public void handleSessionDisconnectEvent(final SessionDisconnectEvent event) {
        // unsubscribe for all topics is always called?
        if(true) {
            return;
        }
        Optional<String> sessionIdOpt = getSessionId(event);
        if (sessionIdOpt.isEmpty()) {
            log.error("Session id is not present in subscribe event");
            return;
        }

        String sessionId = sessionIdOpt.get();
        log.info("Remove all connections for sessionId [{}]", sessionId);
        streamConnectionService.getConnections(sessionId).forEach(connection -> {
            String deviceId = connection.getDeviceId();
            try {
                deviceInteraction.stopStream(deviceId, sessionId);
            } catch (Exception e) {
                log.error("Error while stopping stream for device {}", deviceId, e);
                streamConnectionService.removeConnection(sessionId, deviceId);
            }
        });
    }

    private Optional<String> getSessionId(AbstractSubProtocolEvent event) {
        return Optional.of(event)
                .map(AbstractSubProtocolEvent::getMessage)
                .map(SimpMessageHeaderAccessor::wrap)
                .map(SimpMessageHeaderAccessor::getSessionId);
    }

    private Optional<String> getDeviceId(AbstractSubProtocolEvent event) {
        return getWrappedMessage(event)
                .map(this::getTopic)
                .map(d -> d.substring("/topic".length() + 1));
    }

    private Optional<SimpMessageHeaderAccessor> getWrappedMessage(AbstractSubProtocolEvent event) {
        return Optional.of(event)
                .map(AbstractSubProtocolEvent::getMessage)
                .map(SimpMessageHeaderAccessor::wrap);
    }

    private String getTopic(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        if (StringUtils.isNotBlank(simpMessageHeaderAccessor.getDestination())) {
            return simpMessageHeaderAccessor.getDestination();
        }
        return simpMessageHeaderAccessor.getSubscriptionId();
    }

    private Optional<String> getSubscriptionId(AbstractSubProtocolEvent event) {
        return getWrappedMessage(event)
                .map(SimpMessageHeaderAccessor::getSubscriptionId);
    }
}



