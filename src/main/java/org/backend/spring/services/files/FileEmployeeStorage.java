package org.backend.spring.services.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.Actions;
import org.backend.spring.actions.GetEmployeeFromDto;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.dto.FullEmployeeDto;
import org.backend.spring.exceptions.NotFoundException;
import org.backend.spring.mappers.EmployeeMapper;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.models.Employee;
import org.backend.spring.services.DataStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

import static org.backend.spring.services.utils.Files.deleteFileOrDirectory;

@ConditionalOnProperty(prefix = "data",name = "source",havingValue = "files",matchIfMissing = true)
@Component
@DependsOn("filePostStorage")
@CommonsLog
public class FileEmployeeStorage implements DataStorage<Employee> {
    private Map<UUID, Employee> objects = new HashMap<>();
    @Value("${data.path_employees}")
    private String path_str;
    private final ObjectMapper objectMapper;
    private final EmployeeMapper employeeMapper;
    private Actions actions;

    public FileEmployeeStorage(ObjectMapper objectMapper, MapperBase mapperBase, Actions actions) {
        this.objectMapper = objectMapper;
        this.employeeMapper = mapperBase.getEmployeeMapper();
        this.actions = actions;
    }

    @PostConstruct
    private void postConstructor()
    {
        actions.setEmployeeStorage(this);
        try{
            loadData();
        }
        catch (RuntimeException e)
        {
            log.error("Error load employees data");
        }
    }

    @Override
    public Employee getObject(Filter<Employee> filter) {
        Optional<Employee> tempPostEmployee = objects.values().stream()
                .filter(filter::match)
                .findFirst();
        if(!tempPostEmployee.isPresent())
        {
            throw new NotFoundException("Not found employee object.");
        }
        return tempPostEmployee.get();
    }

    @Override
    public void setObject(Employee object) {

        saveData();
    }

    @Override
    public Employee[] getObjects(Filter<Employee> filter) {
        return objects.values().stream().filter(filter::match).toArray(Employee[]::new);
    }

    @Override
    public void addObject(Employee object) {
        objects.put(object.getId(),object);
        saveData();
    }

    @Override
    public boolean removeObject(Filter<Employee> filter) {
        if(objects.values().stream().noneMatch(filter::matchStrictly))
        {
            throw new NotFoundException("Not found employee object.");
        }
        for (Employee employee:
                objects.values().toArray(new Employee[0])) {
            if(filter.matchStrictly(employee))
            {
                objects.remove(employee.getId());
            }
        }
        saveData();
        return true;
    }

    private void loadData()
    {
        String employees_string;
        FullEmployeeDto[] employees;
        Action<FullEmployeeDto,Employee> action = (Action<FullEmployeeDto, Employee>) actions.get(GetEmployeeFromDto.class);
        try(BufferedReader fileReader = new BufferedReader(new FileReader(path_str))) {
            employees_string = fileReader.lines().collect(Collectors.joining("\n"));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error loading data");
        }

        try {
            employees = objectMapper.readValue(employees_string, FullEmployeeDto[].class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error loading data");
        }
        objects = new HashMap<>();
        for(FullEmployeeDto dto : employees)
        {
            Employee employee = action.execute(dto);
            objects.put(employee.getId(),employee);
        }
    }

    private void saveData()
    {
        String employees_string;
        List<FullEmployeeDto> employees = new ArrayList<>();
        if(!deleteFileOrDirectory(new File(path_str)))
        {
            throw new RuntimeException("Error save employees data");
        }
        if(objects.size() == 0)
        {
            return;
        }
        for(Employee object: objects.values())
        {
            employees.add(employeeMapper.toDto(object,object.getPost().getId().toString()));
        }
        try(FileWriter fw = new FileWriter(path_str))
        {
            fw.write(
                    objectMapper.writeValueAsString(employees.toArray(new FullEmployeeDto[0])));
            fw.flush();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error save employees data ",e);
        }
    }

}
