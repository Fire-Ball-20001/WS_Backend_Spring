package org.backend.spring.controllers.dto.post;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostNoIdDto {
    @NonNull
    private String name;
}
