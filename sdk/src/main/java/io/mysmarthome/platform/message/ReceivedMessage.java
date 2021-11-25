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
}
