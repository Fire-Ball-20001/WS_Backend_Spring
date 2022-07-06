package org.backend.spring.actions;

import lombok.AllArgsConstructor;
import org.backend.spring.actions.filters.PostFilter;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.utils.PostUtils;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GetPostAction implements Action<String, PostEmployee>{

    private final DataStorage<PostEmployee> postStorage;
    @Override
    public PostEmployee execute(String argument) {
        return postStorage.getOptional(PostFilter.builder()
                .id(argument)
                .build()).orElseGet(PostUtils::getDefaultPost);
    }
}
