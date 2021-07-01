package io.mysmarthome.util;

@FunctionalInterface
public interface SneakyRunnable<E extends Exception> {
    void run() throws E;
}
