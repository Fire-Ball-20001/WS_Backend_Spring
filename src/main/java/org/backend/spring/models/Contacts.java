package org.backend.spring.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Contacts {
    String phone;
    String email;
    String workEmail;
}
