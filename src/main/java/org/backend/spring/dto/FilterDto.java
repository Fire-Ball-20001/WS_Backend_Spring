package org.backend.spring.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.backend.spring.models.JobType;

@Getter
@Setter
@Builder
public class FilterDto {
    private String id;
    private String firstName;
    private String lastName;
    private String description;
    private String postName;
    private String postId;
    private String[] characteristics;
    private String phone;
    private String email;
    private String workEmail;
    private JobType jobType;
    public Boolean isStrictly = false;

}
