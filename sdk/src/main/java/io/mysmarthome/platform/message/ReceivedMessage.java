package io.mysmarthome.platform.message;

import io.mysmarthome.device.Device;
import io.mysmarthome.platform.PlatformPlugin;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ReceivedMessage {

    PlatformPlugin<? extends Device> platform;

    Object message;

    Instant receivedAt = Instant.now();

    public static ReceivedMessage createSimpleMessage(String value) {
        return ReceivedMessage.builder()
                .message(new SimpleMessage(value))
                .build();
    }
}
