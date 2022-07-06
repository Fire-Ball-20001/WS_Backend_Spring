package org.backend.spring.services;

import org.backend.spring.actions.filters.Filter;

import java.util.Optional;

public interface DataStorage<T> {
    T get(Filter<T> filter);
    Optional<T> getOptional(Filter<T> filter);
    void set(T object);
    T[] getArray(Filter<T> filter);
    void add(T object);
    boolean remove(Filter<T> filter);
}
