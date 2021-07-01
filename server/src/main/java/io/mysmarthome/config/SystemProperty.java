package io.mysmarthome.config;

import org.apache.commons.lang3.StringUtils;

public enum SystemProperty {
    DEVICE_FILE("deviceFile"),
    CONFIGURATION_FILE("configurationFile");

    private final String property;

    SystemProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        String value = System.getProperty(property);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(String.format("Property '%s' not found", property));
        }
        return value;
    }
}
