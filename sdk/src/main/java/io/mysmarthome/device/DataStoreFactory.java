package io.mysmarthome.device;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DataStoreFactory {

    public static DataStore getDeviceData(String type) {
        if ("simple".equals(type)) {
            log.debug("Create simple device data");
            return new SimpleDataStore();
        }
        throw new IllegalArgumentException("Invalid DeviceData type " + type);
    }
}
