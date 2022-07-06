package org.backend.spring.dto.employee;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.backend.spring.models.JobType;

import java.util.UUID;

@Builder
@Getter
public class EmployeeDto {
    @NonNull
    private final UUID id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Builder.Default
    private String description = "";
    @NonNull
    private String postId;
    @Builder.Default
    private String image = "";
    @NonNull
    private String[] characteristics;
    @NonNull
    private JobType jobType;
    @NonNull
    private ContactsDto contacts;


    public EmployeeDto(@NonNull UUID id, @NonNull String firstName, @NonNull String lastName, String description, @NonNull String postId, String image, @NonNull String[] characteristics, @NonNull JobType jobType, @NonNull ContactsDto contacts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = (description == null ? "" : description);
        this.postId = postId;
        this.image = (image == null ? "" : image);
        this.characteristics = characteristics;
        this.contacts = contacts;
        this.jobType = jobType;
    }

}
