package io.mysmarthome.api;

import io.mysmarthome.configuration.ApplicationProperties;
import io.mysmarthome.model.Serializer;
import io.mysmarthome.platform.message.ReceivedMessage;
import io.mysmarthome.service.DeviceSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceSender deviceSender;
    private final ApplicationProperties applicationProperties;
    private final Serializer serializer;

    @PostMapping("/{deviceId}")
    public Object getGroups(@PathVariable("deviceId") String deviceId, @RequestBody Object msg) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("Receive action request for device {}: {}", deviceId, msg);
        return deviceSender
                .send(deviceId, msg)
                .get(applicationProperties.getInt("api.timeout"), TimeUnit.SECONDS)
                .map(ReceivedMessage::getMessage)
                .map(serializer::serialize)
                .orElseThrow(() -> new IllegalArgumentException("Not able to get response from " + deviceId));
    }
}

