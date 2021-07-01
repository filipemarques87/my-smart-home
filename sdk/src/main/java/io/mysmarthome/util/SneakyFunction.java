package io.mysmarthome.util;

@FunctionalInterface
public interface SneakyFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}
