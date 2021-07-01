package io.mysmarthome.model;

public interface Serializer {

    String serialize(Object obj);

    <T> T deserialize(String jsonStr, Class<T> clazz);
}
