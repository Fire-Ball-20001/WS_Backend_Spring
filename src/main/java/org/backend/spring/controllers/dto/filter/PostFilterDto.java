package org.backend.spring.controllers.dto.filter;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterDto {
     private String id;
     private String name;
     public Boolean isStrictly = false;
}
