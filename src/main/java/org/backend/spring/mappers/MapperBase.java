package org.backend.spring.mappers;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class MapperBase {
    public EmployeeMapper getEmployeeMapper()
    {
        return Mappers.getMapper(EmployeeMapper.class);
    }
    public FilterMapper getFilterMapper() {return Mappers.getMapper(FilterMapper.class);}
    public PostMapper getPostMapper()
    {
        return Mappers.getMapper(PostMapper.class);
    }
}
