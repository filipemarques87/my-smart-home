package io.mysmarthome.model.entity.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Converter
public class DateConverter implements AttributeConverter<Date, Long> {

    @Override
    public Long convertToDatabaseColumn(Date date) {
        if (Objects.nonNull(date)) {
            return date.getTime();
        }
        return null;
    }

    @Override
    public Date convertToEntityAttribute(Long l) {
        if (Objects.nonNull(l)) {
            return new Date(l);
        }
        return null;
    }
}
