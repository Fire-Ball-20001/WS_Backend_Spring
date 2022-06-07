package org.backend.spring.actions;

import lombok.AllArgsConstructor;
import org.backend.spring.actions.filters.PostFilter;
import org.backend.spring.dto.FullEmployeeDto;
import org.backend.spring.exceptions.NotFoundException;
import org.backend.spring.mappers.EmployeeMapper;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.services.utils.PostUtils;

import java.util.Optional;

@AllArgsConstructor
public class GetEmployeeFromDto implements Action<FullEmployeeDto,Employee>{
    DataStorage<PostEmployee> postStorage;
    DataStorage<Employee> employeeDataStorage;
    EmployeeMapper mapper;
    @Override
    public Employee execute(FullEmployeeDto object) {
        PostEmployee postEmployee;
        try {
            postEmployee = postStorage.getObject(new PostFilter(null, object.getPostId(), true));
        }
        catch (NotFoundException e)
        {
            postEmployee = PostUtils.getDefaultPost();
        }
        return mapper.toEntity(object, postEmployee);
    }
}

