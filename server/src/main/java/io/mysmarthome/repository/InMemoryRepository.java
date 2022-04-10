package io.mysmarthome.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<T extends Identifiable> implements CrudRepository<T, String> {

    private final Map<String, T> connections = new ConcurrentHashMap<>();

    @Override
    public <S extends T> S save(S s) {
        connections.put(s.getId(), s);
        return s;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(s -> connections.put(s.getId(), s));
        return iterable;
    }

    @Override
    public Optional<T> findById(String s) {
        return Optional.ofNullable(connections.get(s));
    }

    @Override
    public boolean existsById(String s) {
        return connections.containsKey(s);
    }

    @Override
    public Iterable<T> findAll() {
        return connections.values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<String> iterable) {
        List<T> result = new ArrayList<>();
        iterable.forEach(s -> result.add(connections.get(s)));
        return result;
    }

    @Override
    public long count() {
        return connections.size();
    }

    @Override
    public void deleteById(String s) {
        connections.remove(s);
    }

    @Override
    public void delete(T streamConnection) {
        deleteById(streamConnection.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends T> iterable) {
        iterable.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        connections.clear();
    }
}