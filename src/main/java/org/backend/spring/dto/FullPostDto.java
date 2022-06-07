package org.backend.spring.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Getter
public class FullPostDto {
    @NonNull
    private String name;
    @NonNull
    @Builder.Default
    private UUID id = UUID.randomUUID();
}
