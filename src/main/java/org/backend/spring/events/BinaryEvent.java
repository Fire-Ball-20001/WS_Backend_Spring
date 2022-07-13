package org.backend.spring.events;

import java.util.function.BiFunction;

public interface BinaryEvent<T, S> {
    void call(T first, S second);

    void addListener(BiFunction<T, S, Void> listener);

    void removeListener(BiFunction<T, S, Void> listener);
}
