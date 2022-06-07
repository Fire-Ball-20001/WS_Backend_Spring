package org.backend.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterDto {
    private String id;
    private String firstName;
    private String lastName;
    private String description;
    private String postName;
    private String postId;
    private String[] characteristics;
    public boolean isStrictly;

    public void setIsStrictly(boolean new_value)
    {
        isStrictly = new_value;
    }
}
