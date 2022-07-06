package org.backend.spring.controllers.employeeController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.filters.EmployeeFilter;
import org.backend.spring.dto.employee.EmployeeDto;
import org.backend.spring.dto.employee.EmployeeNoIdDto;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.mappers.MapperBase;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@AllArgsConstructor
public class EmployeeRestController {

    DataStorage<Employee> storage;
    ObjectMapper objectMapper;
    MapperBase mapperBase;
    Action<String, PostEmployee> getPostAction;

    @GetMapping("/employee")
    @Operation(summary = "Get one employee",tags = "Employee")
    public EmployeeDto getEmployee(FilterDto filter)
    {
        EmployeeFilter employeeFilter = mapperBase.getFilterMapper().toEmployeeEntity(filter);
        Employee find = storage.get(employeeFilter);
        return mapperBase.getEmployeeMapper().toDto(find,find.getPost().getId().toString());
    }
    @GetMapping("/employees")
    @Operation(summary = "Get more employees with filter",tags = "Employee")
    public EmployeeDto[] getEmployees(FilterDto filter)
    {
        EmployeeFilter employeeFilter = mapperBase.getFilterMapper().toEmployeeEntity(filter);
        Employee[] employees = storage.getArray(employeeFilter);
        return Arrays.stream(employees).map(
                employee -> mapperBase.getEmployeeMapper()
                                .toDto(employee,employee.getPost().getId().toString()))
                .toArray(EmployeeDto[]::new);
    }

    @PostMapping("/employee/set")
    @Operation(summary = "Replace employee",tags = "Employee")
    public ObjectNode setEmployee(@RequestBody EmployeeDto employeeDto)
    {
        Employee employee = mapperBase.getEmployeeMapper().toEntity(
                employeeDto,
                getPostAction.execute(employeeDto.getPostId()));
        storage.set(employee);
        return objectMapper.createObjectNode().put("id",employee.getId().toString());
    }

    @PostMapping("/employee/add")
    @Operation(summary = "Add new employee",tags = "Employee")
    public ObjectNode addEmployee(@RequestBody EmployeeNoIdDto employeeNoIdDto)
    {
        Employee employee = mapperBase.getEmployeeMapper().toEntity(
                employeeNoIdDto,
                getPostAction.execute(employeeNoIdDto.getPostId()));
        storage.add(employee);
        return objectMapper.createObjectNode().put("id",employee.getId().toString());
    }

    @DeleteMapping("/employee/remove")
    @Operation(summary = "Remove employee with strictly filter",tags = "Employee")
    public ObjectNode removeEmployee(@RequestBody FilterDto filter)
    {
        EmployeeFilter employeeFilter = mapperBase.getFilterMapper().toEmployeeEntity(filter);
        return objectMapper.createObjectNode().put("status",storage.remove(employeeFilter));
    }

}
