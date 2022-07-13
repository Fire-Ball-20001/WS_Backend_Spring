package org.backend.spring.actions;

import lombok.AllArgsConstructor;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.utils.FilterUtils;
import org.backend.spring.utils.PostUtils;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GetPostAction implements Action<String, PostEmployee> {

    private final DataStorage<PostEmployee> postStorage;

    @Override
    public PostEmployee execute(String argument) {
        return postStorage.getOptional(new Filter<PostEmployee>().addOperation(
                (PostEmployee post) -> FilterUtils.isApproximatelyMatch(post.getId().toString(),argument))).orElseGet(PostUtils::getDefaultPost);
    }
}
