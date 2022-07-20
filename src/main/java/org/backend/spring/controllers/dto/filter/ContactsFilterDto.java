package org.backend.spring.controllers.dto.filter;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactsFilterDto {
    private String phone;
    private String email;
    private String workEmail;
}
