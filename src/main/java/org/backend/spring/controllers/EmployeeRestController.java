package org.backend.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.filters.EmployeeFilter;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.dto.FullEmployeeDto;
import org.backend.spring.dto.PartEmployeeDto;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.models.Employee;
import org.backend.spring.services.DataStorage;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class EmployeeRestController {

    DataStorage<Employee> storage;
    ObjectMapper objectMapper;
    MapperBase mapperBase;
    Action<PartEmployeeDto,Employee> partAction;
    Action<FullEmployeeDto,Employee> fullAction;

    public EmployeeRestController(DataStorage<Employee> storage, ObjectMapper mapper, MapperBase mapperBase,Action<PartEmployeeDto,Employee> partAction, Action<FullEmployeeDto,Employee> fullAction) {
        this.storage = storage;
        this.mapperBase = mapperBase;
        objectMapper = mapper;
        this.partAction = partAction;
        this.fullAction = fullAction;
    }

    @GetMapping("/employee")
    @Operation(summary = "Get one employee",tags = "Employee")
    public FullEmployeeDto getEmployee(FilterDto filter)
    {
        EmployeeFilter employeeFilter = mapperBase.getFilterMapper().toEmployeeEntity(filter);
        Employee find = storage.getObject(employeeFilter);
        return mapperBase.getEmployeeMapper().toDto(find,find.getPostId().toString());
    }
    @GetMapping("/employees")
    @Operation(summary = "Get more employees with filter",tags = "Employee")
    public FullEmployeeDto[] getEmployees(FilterDto filter)
    {
        EmployeeFilter employeeFilter = mapperBase.getFilterMapper().toEmployeeEntity(filter);
        Employee[] employees = storage.getObjects(employeeFilter);
        return Arrays.stream(employees).map(
                employee -> mapperBase.getEmployeeMapper()
                                .toDto(employee,employee.getPostId().toString()))
                .toArray(FullEmployeeDto[]::new);
    }

    @PostMapping("/employee/set")
    @Operation(summary = "Replace employee",tags = "Employee")
    public ObjectNode setEmployee(@RequestBody FullEmployeeDto fullEmployeeDto)
    {
        Employee employee = fullAction.execute(fullEmployeeDto);
        storage.setObject(employee);
        return objectMapper.createObjectNode().put("id",employee.getId().toString());
    }

    @PostMapping("/employee/add")
    @Operation(summary = "Add new employee",tags = "Employee")
    public ObjectNode addEmployee(@RequestBody PartEmployeeDto partEmployeeDto)
    {
        Employee employee = partAction.execute(partEmployeeDto);
        storage.addObject(employee);
        return objectMapper.createObjectNode().put("id",employee.getId().toString());
    }

    @DeleteMapping("/employee/remove")
    @Operation(summary = "Remove employee with strictly filter",tags = "Employee")
    public ObjectNode removeEmployee(@RequestBody FilterDto filter)
    {
        EmployeeFilter employeeFilter = mapperBase.getFilterMapper().toEmployeeEntity(filter);
        return objectMapper.createObjectNode().put("status",storage.removeObject(employeeFilter));
    }

}
