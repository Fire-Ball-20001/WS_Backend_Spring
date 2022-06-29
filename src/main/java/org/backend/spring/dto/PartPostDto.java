package org.backend.spring.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartPostDto {
    @NonNull
    private String name;
}
