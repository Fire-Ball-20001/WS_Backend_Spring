package org.backend.spring.actions.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor
@Getter
public class Filter<T> {
    Predicate<T> predicate = t -> true;

    public boolean match(T object) {
        return predicate.test(object);
    }
    public Filter<T> addOperation(Function<T, Boolean> function) {
        predicate = predicate.and(function::apply);
        return this;
    }

    public Filter<T> addFilter(Filter<T> filter) {
        predicate = predicate.and(filter.getPredicate());
        return this;
    }
}
