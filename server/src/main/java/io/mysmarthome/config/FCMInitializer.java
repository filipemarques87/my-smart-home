package io.mysmarthome.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FCMInitializer {

    @Value("${firebaseConfigFile:#{null}}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        if (Objects.isNull(firebaseConfigPath)) {
            log.info("FCM not initialized - missing key file");
            return;
        }

        File file = new File(firebaseConfigPath);
        try (InputStream in = new FileInputStream(file)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error("Error on initializing FCM", e);
        }
    }
}
