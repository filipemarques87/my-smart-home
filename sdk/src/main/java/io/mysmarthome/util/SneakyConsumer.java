package io.mysmarthome.util;

@FunctionalInterface
public interface SneakyConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
