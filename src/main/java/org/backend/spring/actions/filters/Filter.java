package org.backend.spring.actions.filters;

public interface Filter<T> {
    boolean matchStrictly(T object);
    boolean matchApproximately(T object);
    boolean match(T object);
}
