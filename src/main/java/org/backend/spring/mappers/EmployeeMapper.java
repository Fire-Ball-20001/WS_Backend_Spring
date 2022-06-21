package org.backend.spring.mappers;

import org.backend.spring.dto.FullEmployeeDto;
import org.backend.spring.dto.PartEmployeeDto;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmployeeMapper {
    @Mapping(target = "post",source = "post")
    @Mapping(target = "id",source = "dto.id")
    Employee toEntity(FullEmployeeDto dto, PostEmployee post);
    @Mapping(target = "post",source = "post")
    @Mapping(target = "id",expression = "java(java.util.UUID.randomUUID())")
    Employee toEntity(PartEmployeeDto dto, PostEmployee post);
    @Mapping(target = "postId",source = "postId")
    FullEmployeeDto toDto(Employee object,String postId);
}
