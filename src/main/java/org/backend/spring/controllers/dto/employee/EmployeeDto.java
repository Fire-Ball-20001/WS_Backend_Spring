package org.backend.spring.controllers.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.backend.spring.controllers.dto.post.PostDto;
import org.backend.spring.models.JobType;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class EmployeeDto {
    @NonNull
    private final UUID id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String description;
    @NonNull
    private PostDto post;
    private String image;
    @NonNull
    private String[] characteristics;
    @NonNull
    private JobType jobType;
    @NonNull
    private ContactsDto contacts;

}
