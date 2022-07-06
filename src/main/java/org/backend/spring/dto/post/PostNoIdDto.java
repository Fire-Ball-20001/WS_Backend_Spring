package org.backend.spring.dto.post;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostNoIdDto {
    @NonNull
    private String name;
}
