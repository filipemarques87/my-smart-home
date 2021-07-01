package io.mysmarthome.device;

public interface DataStore {
    void add(Object newData);

    Object getLast();

//    Object getAll();

}
