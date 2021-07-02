package io.mysmarthome.platform.message;

import io.mysmarthome.device.Device;

@FunctionalInterface
public interface OnReceive {
    void onReceive(Device device, ReceivedMessage msg);
}
