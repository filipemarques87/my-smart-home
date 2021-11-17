package io.mysmarthome.platform.message;

import io.mysmarthome.device.Device;
import io.mysmarthome.platform.PlatformPlugin;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class ReceivedMessage {

    PlatformPlugin<? extends Device> platform;

    Object message;

    Date receivedAt = new Date();
}
