package org.backend.spring.mappers;

import org.backend.spring.actions.filters.EmployeeFilter;
import org.backend.spring.actions.filters.PostFilter;
import org.backend.spring.dto.FilterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FilterMapper {
    @Mapping(target = "id",expression = "java(filterDto.getPostId())")
    @Mapping(target = "name",expression = "java(filterDto.getPostName())")
    @Mapping(target = "isStrictly",source = "filterDto.isStrictly")
    PostFilter toPostEntity(FilterDto filterDto);



    @Mapping(target = "postFilter.id",source = "dto.postId")
    @Mapping(target = "postFilter.name",source = "dto.postName")
    @Mapping(target = "postFilter.isStrictly",source = "dto.isStrictly")
    @Mapping(target = "isStrictly",source = "dto.isStrictly")
    @Mapping(target = "contactsFilter.phone",source = "dto.phone")
    @Mapping(target = "contactsFilter.email",source = "dto.email")
    @Mapping(target = "contactsFilter.workEmail",source = "dto.workEmail")
    EmployeeFilter toEmployeeEntity(FilterDto dto);

    @Mapping(target = "postId",expression = "java(filter.getId())")
    @Mapping(target = "postName",expression = "java(filter.getName())")
    FilterDto toDto(PostFilter filter);

    @Mapping(target = "postId",expression = "java(filter.getPostFilter().getId())")
    @Mapping(target = "postName",expression = "java(filter.getPostFilter().getName())")
    @Mapping(target = "phone",expression = "java(filter.getContactsFilter().getPhone())")
    @Mapping(target = "email",expression = "java(filter.getContactsFilter().getEmail())")
    @Mapping(target = "workEmail",expression = "java(filter.getContactsFilter().getWorkEmail())")
    FilterDto toDto(EmployeeFilter filter);

}
