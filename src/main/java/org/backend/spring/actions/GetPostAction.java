package org.backend.spring.actions;

import lombok.AllArgsConstructor;
import org.backend.spring.actions.filters.PostFilter;
import org.backend.spring.exceptions.NotFoundException;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.services.utils.PostUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class GetPostAction implements Action<UUID,PostEmployee> {
    DataStorage<PostEmployee> storage;

    @Override
    public PostEmployee execute(UUID object) {
        PostEmployee postEmployee;
        try {
            postEmployee = storage.getObject(new PostFilter(null, object.toString(), true));
        }
        catch (NotFoundException e)
        {
            postEmployee = PostUtils.getDefaultPost();
        }
        return postEmployee;
    }
}
