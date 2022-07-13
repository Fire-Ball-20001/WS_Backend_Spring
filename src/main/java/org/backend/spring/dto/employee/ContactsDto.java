package org.backend.spring.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;

@AllArgsConstructor
@Getter
public class ContactsDto {
    private String phone;
    @Email
    private String email;
    @Email
    private String workEmail;
}
