package org.backend.spring.actions.filters;

import lombok.Builder;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;

import java.util.UUID;

@Builder
public class EmployeeFilter implements Filter<Employee>{
    private String id;
    private String firstName;
    private String lastName;
    private String description;
    private PostFilter postFilter;
    private String image = "";
    private String[] characteristics;

    @Override
    public boolean match(Employee object) {
        return false;
    }

    @Override
    public boolean matchApproximately(Employee object) {
        return false;
    }
}
