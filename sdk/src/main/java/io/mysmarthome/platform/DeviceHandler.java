package io.mysmarthome.platform;

import io.mysmarthome.device.Device;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Data
public class DeviceHandler {

    @Setter(AccessLevel.NONE)
    private Device device;

    @Setter(AccessLevel.NONE)
    private OnReceive callback;

    private List<OnReceive> tempCallback;


    @Builder
    public DeviceHandler(Device device, OnReceive callback) {
        this.device = device;
        this.callback = callback;
        tempCallback = new CopyOnWriteArrayList<>();
    }

    public void addTempCallback(OnReceive callback) {
        tempCallback.add(callback);
    }

    public void broadcastMessage(ReceivedMessage message) {
        try {
            if (callback != null) {
                callback.onReceive(device, message);
            }
            tempCallback.forEach(c -> c.onReceive(device, message));
        } catch (Throwable t) {
            log.error("Error while handle the mqtt response", t);
        }
        tempCallback.clear();
    }
}
