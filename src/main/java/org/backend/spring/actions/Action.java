package org.backend.spring.actions;

public interface Action<T, R> {
    R execute(T object);
}
