package io.mysmarthome.model.entity.converter;

import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LobSerializerConverter implements AttributeConverter<Object, byte[]> {

    @SneakyThrows
    @Override
    public byte[] convertToDatabaseColumn(Object data) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ObjectOutputStream objOs = new ObjectOutputStream(os)) {

            objOs.writeObject(data);
            return os.toByteArray();
        }
    }

    @SneakyThrows
    @Override
    public Object convertToEntityAttribute(byte[] lob) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(lob);
             ObjectInputStream objIs = new ObjectInputStream(is)) {

            return objIs.readObject();
        }
    }
}
