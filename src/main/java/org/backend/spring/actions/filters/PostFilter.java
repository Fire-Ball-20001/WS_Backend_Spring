package org.backend.spring.actions.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.backend.spring.models.PostEmployee;
import static org.backend.spring.utils.BooleanUtils.*;

import java.util.UUID;
@Builder
@Getter
@AllArgsConstructor
public class PostFilter implements Filter<PostEmployee>{
    private String name;
    private String id;
    @Builder.Default
    private boolean isStrictly = false;

    @Override
    public boolean match(PostEmployee object) {
        if(isStrictly)
        {
            return matchStrictly(object);
        }
        else
        {
            return matchApproximately(object);
        }
    }
    @Override
    public boolean matchStrictly(PostEmployee object) {
        if(name != null)
        {
            if(!object.getName().equals(name))
            {
                return false;
            }
        }
        if (id != null) {
            UUID uuid;
            try {
                uuid = UUID.fromString(id);
            } catch (Exception e) {
                return false;
            }
            return object.getId().equals(uuid);
        }
        return true;
    }

    @Override
    public boolean matchApproximately(PostEmployee object) {
        if(name != null)
        {
            if(!isApproximatelyMatch(object.getName(), name))
            {
                return false;
            }
        }
        if (id != null) {
            return isApproximatelyMatch(object.getId().toString(),id);
        }
        return true;
    }


}
