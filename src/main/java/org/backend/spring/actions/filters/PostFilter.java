package org.backend.spring.actions.filters;

import lombok.Builder;
import org.backend.spring.models.PostEmployee;
import java.util.UUID;
@Builder
public class PostFilter implements Filter<PostEmployee>{
    private String name;
    private String id;
    @Override
    public boolean match(PostEmployee object) {
        return false;
    }

    @Override
    public boolean matchApproximately(PostEmployee object) {
        return false;
    }
}
