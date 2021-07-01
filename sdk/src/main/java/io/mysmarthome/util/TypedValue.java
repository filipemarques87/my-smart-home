package io.mysmarthome.util;

import java.util.Optional;

public class TypedValue {

    private final Object object;

    public TypedValue() {
        this(null);
    }

    public TypedValue(Object object) {
        this.object = object;
    }

    public String asString() {
        return asString(null);
    }

    public String asString(String defaultValue) {
        return as(String.class, defaultValue);
    }

    public <T> T as(Class<T> clazz) {
        return as(clazz, null);
    }

    public <T> T as(Class<T> clazz, T defaultValue) {
        return Optional.ofNullable(object)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .orElse(defaultValue);
    }

    public Object raw() {
        return object;
    }
}
