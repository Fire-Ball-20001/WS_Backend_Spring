package org.backend.spring.controllers.dto.filter;

import lombok.*;
import org.backend.spring.models.JobType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeFilterDto {
    private String id;
    private String firstName;
    private String lastName;
    private String description;
    private PostFilterDto post;
    private String[] characteristics;
    private ContactsFilterDto contacts;
    private JobType jobType;

}
