package io.mysmarthome.util;

@FunctionalInterface
public interface SneakySupplier<T, E extends Exception> {
    T get() throws E;
}
