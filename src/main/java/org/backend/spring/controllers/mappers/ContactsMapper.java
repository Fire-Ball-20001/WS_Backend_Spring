package org.backend.spring.controllers.mappers;

import org.backend.spring.dto.employee.ContactsDto;
import org.backend.spring.models.Contacts;
import org.mapstruct.Mapper;

@Mapper
public interface ContactsMapper {
    Contacts toEntity(ContactsDto dto);

    ContactsDto toDto(Contacts object);
}
