package org.backend.spring.controllers.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.backend.spring.controllers.dto.post.PostDto;
import org.backend.spring.models.JobType;


@Builder
@Getter
@AllArgsConstructor
public class EmployeeNoIdUsePostIdDto {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Builder.Default
    @NonNull
    private String description = "";
    @NonNull
    private String postId;
    @Builder.Default
    @NonNull
    private String image = "";
    @NonNull
    private String[] characteristics;
    @NonNull
    private ContactsDto contacts;
    @NonNull
    private JobType jobType;
}
