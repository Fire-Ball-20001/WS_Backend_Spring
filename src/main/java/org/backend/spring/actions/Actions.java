package org.backend.spring.actions;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.backend.spring.mappers.EmployeeMapper;
import org.backend.spring.mappers.FilterMapper;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.mappers.PostMapper;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@CommonsLog
public class Actions {
    @Setter
    private DataStorage<? extends Employee> employeeStorage;
    private final DataStorage<? extends PostEmployee> postStorage;
    private final MapperBase baseMapper;



    public Action<?,?> get(Class<? extends Action<?,?>> actionClass)
    {
        Constructor<?> cons = actionClass.getConstructors()[0];
        List<Object> args = new ArrayList<>();
        Type[] types = cons.getParameterTypes();
        args.add(postStorage);
        args.add(employeeStorage);
        for(int i = 2;i<types.length;i++)
        {
            if(types[i] == PostMapper.class)
            {
                args.add(baseMapper.getPostMapper());
            }
            else if(types[i] == FilterMapper.class)
            {
                args.add(baseMapper.getFilterMapper());
            }
            else if(types[i] == EmployeeMapper.class)
            {
                args.add(baseMapper.getEmployeeMapper());
            }
            else if(types[i] == MapperBase.class)
            {
                args.add(baseMapper);
            }
        }
        try {
            return (Action<?, ?>) cons.newInstance(args.toArray());
        }
        catch (Exception e)
        {
            log.error("Error create action", e);
        }
        return null;
    }

}
