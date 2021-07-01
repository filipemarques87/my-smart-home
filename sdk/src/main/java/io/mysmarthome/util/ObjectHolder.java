package io.mysmarthome.util;

public class ObjectHolder<T> {

    private T value;

    public synchronized void set(T value) {
        this.value = value;
    }

    public synchronized T get() {
        return value;
    }
}
