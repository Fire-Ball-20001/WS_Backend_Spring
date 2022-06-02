package org.backend.spring.actions.filters;

public interface Filter<T> {
    boolean match(T object);
    boolean matchApproximately(T object);
}
