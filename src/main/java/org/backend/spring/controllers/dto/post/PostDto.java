package org.backend.spring.controllers.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Getter
public class PostDto {
    @NonNull
    private String name;
    @NonNull
    @Builder.Default
    private UUID id = UUID.randomUUID();
}
