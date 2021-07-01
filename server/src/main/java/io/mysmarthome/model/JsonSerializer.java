package io.mysmarthome.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class JsonSerializer implements Serializer {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public String serialize(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    @SneakyThrows
    @Override
    public <T> T deserialize(String jsonStr, Class<T> clazz) {
        return mapper.readValue(jsonStr, clazz);
    }
}
