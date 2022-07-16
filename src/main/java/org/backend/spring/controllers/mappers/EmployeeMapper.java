package org.backend.spring.controllers.mappers;

import org.backend.spring.controllers.dto.employee.EmployeeDto;
import org.backend.spring.controllers.dto.employee.EmployeeNoIdDto;
import org.backend.spring.controllers.dto.employee.EmployeeNoIdUsePostIdDto;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface EmployeeMapper {
    Employee toEntity(EmployeeDto dto);

    @Mapping(target = "post", source = "post")
    @Mapping(target = "id", source = "id")
    Employee toEntity(EmployeeNoIdUsePostIdDto dto, PostEmployee post, UUID id);
    @Mapping(target = "id", source = "id")
    Employee toEntity(EmployeeNoIdDto dto, UUID id);

    EmployeeDto toDto(Employee object);
}
