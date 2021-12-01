package io.mysmarthome;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstants {
    public static final String APP_ROOT_FOLDER = System.getProperty("appFolder");
    public static final String CONFIG_FOLDER = Paths.get(APP_ROOT_FOLDER, "config").toString();
    public static final String DATA_FOLDER = Paths.get(APP_ROOT_FOLDER, "data").toString();
    public static final String LOGS_FOLDER = Paths.get(APP_ROOT_FOLDER, "logs").toString();
    public static final String PLATFORMS_FOLDER = Paths.get(APP_ROOT_FOLDER, "platforms").toString();
    public static final String DEVICES_FILE = Paths.get(CONFIG_FOLDER, "devices.yaml").toString();
    public static final String CONFIG_FILE = Paths.get(CONFIG_FOLDER, "configuration.properties").toString();
}
