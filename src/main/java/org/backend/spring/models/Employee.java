package org.backend.spring.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class Employee {
    @NonNull
    @Builder.Default
    private final UUID id = UUID.randomUUID();
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    @Builder.Default
    private String description = "";
    @NonNull
    private PostEmployee post;
    @NonNull
    @Builder.Default
    private String image = "";
    @NonNull
    private String[] characteristics;
}
