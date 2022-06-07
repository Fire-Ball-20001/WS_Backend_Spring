package org.backend.spring.services;

import org.backend.spring.actions.filters.Filter;

public interface DataStorage<T> {
    T getObject(Filter<T> filter);
    void setObject(T object);
    T[] getObjects(Filter<T> filter);
    void addObject(T object);
    boolean removeObject(Filter<T> filter);
}
