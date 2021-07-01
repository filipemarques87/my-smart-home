package io.mysmarthome.device;

public class SimpleDataStore implements DataStore {

    Object data;

    @Override
    public void add(Object newData) {
        data = newData;
    }

    @Override
    public Object getLast() {
        return data;
    }
}
