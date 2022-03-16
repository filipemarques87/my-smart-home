package io.mysmarthome.model.entity.converter;

import io.mysmarthome.model.SendOnConditionTrigger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SendOnConditionListConverter implements AttributeConverter<Set<SendOnConditionTrigger>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<SendOnConditionTrigger> sendOnConditionTriggerList) {
        if (sendOnConditionTriggerList == null) {
            return null;
        }
        return sendOnConditionTriggerList.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<SendOnConditionTrigger> convertToEntityAttribute(String s) {
        if (StringUtils.isBlank(s)) {
            return new HashSet<>();
        }
        return Arrays.stream(s.split(DELIMITER))
                .map(SendOnConditionTrigger::valueOf)
                .collect(Collectors.toSet());
    }
}
