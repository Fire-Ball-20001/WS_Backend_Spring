package org.backend.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContactsDto {
    private String phone;
    private String email;
    private String workEmail;
}
