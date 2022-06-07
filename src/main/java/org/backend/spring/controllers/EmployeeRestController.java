package org.backend.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.Actions;
import org.backend.spring.actions.GetEmployeeFromDto;
import org.backend.spring.actions.GetEmployeeFromPartDto;
import org.backend.spring.actions.filters.EmployeeFilter;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.dto.FullEmployeeDto;
import org.backend.spring.dto.PartEmployeeDto;
import org.backend.spring.mappers.EmployeeMapper;
import org.backend.spring.mappers.FilterMapper;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.models.Employee;
import org.backend.spring.services.DataStorage;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class EmployeeRestController {

    DataStorage<Employee> storage;
    EmployeeMapper employeeMapper;
    ObjectMapper objectMapper;
    FilterMapper filterMapper;
    Actions actions;

    public EmployeeRestController(DataStorage<Employee> storage, ObjectMapper mapper, MapperBase mapperBase,Actions actions) {
        this.storage = storage;
        this.employeeMapper = mapperBase.getEmployeeMapper();
        objectMapper = mapper;
        this.filterMapper = mapperBase.getFilterMapper();
        this.actions = actions;
    }

    @GetMapping("/employee")
    public FullEmployeeDto getEmployee(FilterDto filter)
    {
        EmployeeFilter employeeFilter = filterMapper.toEmployeeEntity(filter);
        Employee find = storage.getObject(employeeFilter);
        return employeeMapper.toDto(find,find.getPost().getId().toString());
    }
    @GetMapping("/employees")
    public FullEmployeeDto[] getEmployees(FilterDto filter)
    {
        EmployeeFilter employeeFilter = filterMapper.toEmployeeEntity(filter);
        Employee[] employees = storage.getObjects(employeeFilter);
        return Arrays.stream(employees).map(
                employee -> employeeMapper.
                        toDto(employee,employee.getPost().getId().toString()))
                .toArray(FullEmployeeDto[]::new);
    }

    @PostMapping("/employee/set")
    public ObjectNode setEmployee(@RequestBody FullEmployeeDto fullEmployeeDto)
    {
        Employee employee = ((Action<FullEmployeeDto,Employee>)actions.get(GetEmployeeFromDto.class)).execute(fullEmployeeDto);
        storage.setObject(employee);
        return objectMapper.createObjectNode().put("id",employee.getId().toString());
    }

    @PostMapping("/employee/add")
    public ObjectNode addEmployee(@RequestBody PartEmployeeDto partEmployeeDto)
    {
        Employee employee = ((Action<PartEmployeeDto,Employee>)actions.get(GetEmployeeFromPartDto.class)).execute(partEmployeeDto);
        storage.addObject(employee);
        return objectMapper.createObjectNode().put("id",employee.getId().toString());
    }

    @DeleteMapping("/employee/remove")
    public ObjectNode removeEmployee(@RequestBody FilterDto filter)
    {
        EmployeeFilter employeeFilter = filterMapper.toEmployeeEntity(filter);
        return objectMapper.createObjectNode().put("status",storage.removeObject(employeeFilter));
    }

}
