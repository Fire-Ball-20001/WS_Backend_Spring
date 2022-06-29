package org.backend.spring.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Employee {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String description;
    private PostEmployee post;
    private String image;
    private String[] characteristics;
    private Contacts contacts;
    private JobType jobType;
}
