package io.mysmarthome;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import static io.mysmarthome.AppConstants.*;

@Slf4j
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App {
    public static void main(String[] args) throws IOException {
        initApp();
        SpringApplication.run(App.class, args);
    }

    private static void createFolderIfNotExists(String folder) {
        File directory = new File(folder);
        if (directory.mkdirs()) {
            log.info("Creating folder {}", folder);
        }
    }

    private static void initApp() throws IOException {
        log.info("Creating application folders, if not exist");
        Stream.of(CONFIG_FOLDER, DATA_FOLDER, LOGS_FOLDER, PLATFORMS_FOLDER)
                .forEach(App::createFolderIfNotExists);

        log.info("Creating empty devices.yaml file, if not exists");
        File deviceFile = new File(DEVICES_FILE);
        if (!deviceFile.exists()) {
            FileUtils.writeStringToFile(deviceFile, "[]", Charset.defaultCharset());
        }

        log.info("Creating empty configuration.properties file, if not exists");
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            FileUtils.writeStringToFile(configFile, "", Charset.defaultCharset());
        }
    }
}
