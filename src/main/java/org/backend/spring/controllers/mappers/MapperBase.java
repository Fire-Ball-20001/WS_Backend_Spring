package org.backend.spring.controllers.mappers;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MapperBase {
    @Bean
    public EmployeeMapper getEmployeeMapper() {
        return Mappers.getMapper(EmployeeMapper.class);
    }

    @Bean
    public PostMapper getPostMapper() {
        return Mappers.getMapper(PostMapper.class);
    }

    @Bean
    public ContactsMapper getContactsMapper() {
        return Mappers.getMapper(ContactsMapper.class);
    }
}
