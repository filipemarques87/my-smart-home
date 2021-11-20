package io.mysmarthome.model.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Converter
public class CustomInfoConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        try {
            return mapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            log.error("Error on converting object to string", e);
        }
        return null;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        Map<String, Object> customerInfo = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            customerInfo = objectMapper.readValue(s, Map.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return customerInfo;
    }
}
