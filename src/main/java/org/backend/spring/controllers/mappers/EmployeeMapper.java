package org.backend.spring.controllers.mappers;

import org.backend.spring.dto.employee.EmployeeDto;
import org.backend.spring.dto.employee.EmployeeNoIdDto;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface EmployeeMapper {
    @Mapping(target = "post", source = "post")
    @Mapping(target = "id", source = "dto.id")
    Employee toEntity(EmployeeDto dto, PostEmployee post);

    @Mapping(target = "post", source = "post")
    @Mapping(target = "id", source = "id")
    Employee toEntity(EmployeeNoIdDto dto, PostEmployee post, UUID id);

    @Mapping(target = "postId", source = "postId")
    EmployeeDto toDto(Employee object, String postId);
}
