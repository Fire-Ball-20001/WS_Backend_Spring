package org.backend.spring.models;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Employee {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String description;
    private UUID postId;
    private String image;
    private String[] characteristics;
}
