package org.backend.spring.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.backend.spring.models.Contacts;
import org.backend.spring.models.JobType;

@Builder
@Getter
public class PartEmployeeDto {
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
    private ContactsDto contacts;
    @NonNull
    private JobType jobType;

    public PartEmployeeDto(@NonNull String firstName, @NonNull String lastName, String description, @NonNull String postId, String image, @NonNull String[] characteristics, @NonNull ContactsDto contacts, @NonNull JobType jobType) {
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
