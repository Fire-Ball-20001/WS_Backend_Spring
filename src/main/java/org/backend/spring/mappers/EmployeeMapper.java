package org.backend.spring.mappers;

import org.backend.spring.dto.FullEmployeeDto;
import org.backend.spring.dto.PartEmployeeDto;
import org.backend.spring.models.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface EmployeeMapper {
    @Mapping(target = "postId",source = "postId")
    @Mapping(target = "id",source = "dto.id")
    Employee toEntity(FullEmployeeDto dto, UUID postId);
    @Mapping(target = "postId",source = "postId")
    @Mapping(target = "id",expression = "java(java.util.UUID.randomUUID())")
    Employee toEntity(PartEmployeeDto dto, UUID postId);
    @Mapping(target = "postId",source = "postId")
    FullEmployeeDto toDto(Employee object,String postId);
}
