package org.backend.spring.events;

import org.backend.spring.models.PostEmployee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Component("updatePostEvent")
public class UpdatePostsEvent implements BinaryEvent<PostEmployee,PostEmployee>{

    private final List<BiFunction<PostEmployee, PostEmployee, Void>> listeners = new ArrayList<>();

    @Override
    public void call(PostEmployee first, PostEmployee second) {
        for(BiFunction<PostEmployee, PostEmployee, Void> listener : listeners)
        {
            listener.apply(first,second);
        }
    }

    @Override
    public void addListener(BiFunction<PostEmployee, PostEmployee, Void> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(BiFunction<PostEmployee, PostEmployee, Void> listener) {
        listeners.remove(listener);
    }
}
