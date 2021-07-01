package io.mysmarthome.device;

import io.mysmarthome.util.TypedValue;

public interface Device {

    String getDeviceId();

    TypedValue getCustomInfo(String key);
}
