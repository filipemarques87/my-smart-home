package io.mysmarthome.configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class ApplicationProperties {

    private final Properties prop = new Properties();

    public ApplicationProperties(String filePath) {
        load(filePath);
    }

    private void load(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            prop.load(inputStream);
        } catch (Exception e) {
            throw new ApplicationPropertiesException(e);
        }
    }

    public int getInt(String key) {
        return Optional.of(getInt(key, null))
                .orElseThrow();
    }

    public int getInt(String key, Integer defaultInt) {
        return Optional.ofNullable(prop.get(key))
                .map(Object::toString)
                .map(parseInt)
                .orElse(defaultInt);
    }

    public String getString(String key, String defaultValue) {
        return Optional.ofNullable(prop.get(key))
                .map(Object::toString)
                .orElse(defaultValue);
    }

    public String getString(String key) {
        return Optional.ofNullable(getString(key, null))
                .orElseThrow();
    }

    private final Function<String, Integer> parseInt = s -> {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    };
}
