package org.backend.spring.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Getter
public class FullEmployeeDto {
    @NonNull
    private final UUID id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Builder.Default
    private String description = "";
    @NonNull
    private String postId;
    @Builder.Default
    private String image = "";
    @NonNull
    private String[] characteristics;


    public FullEmployeeDto(@NonNull UUID id, @NonNull String firstName, @NonNull String lastName, String description, @NonNull String postId, String image, @NonNull String[] characteristics) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = (description == null ? "" : description);
        this.postId = postId;
        this.image = (image == null ? "" : image);
        this.characteristics = characteristics;
    }

}
