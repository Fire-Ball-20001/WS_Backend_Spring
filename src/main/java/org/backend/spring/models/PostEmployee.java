package org.backend.spring.models;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class PostEmployee {
    @Setter(AccessLevel.PRIVATE)
    private String name;
    @Setter(AccessLevel.PRIVATE)
    private UUID id;

}
