package org.backend.spring.controllers.employeeController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.dto.FilterDto;
import org.backend.spring.dto.employee.EmployeeDto;
import org.backend.spring.dto.employee.EmployeeNoIdDto;
import org.backend.spring.controllers.mappers.MapperBase;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.backend.spring.utils.FilterUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeRestController {

    DataStorage<Employee> storage;
    ObjectMapper objectMapper;
    MapperBase mapperBase;
    Action<String, PostEmployee> getPostAction;

    @GetMapping("/{id}")
    @Operation(summary = "Get one employee, id can be partial.", tags = "Employee")
    public EmployeeDto getEmployee(@PathVariable String id, FilterDto filter) {
        filter.setId(id.toString());
        Filter<Employee> employeeFilter = FilterUtils.parseEmployeeFilter(filter);
        Employee find = storage.get(employeeFilter);
        return mapperBase.getEmployeeMapper().toDto(find, find.getPost().getId().toString());
    }

    @GetMapping("/list")
    @Operation(summary = "Get more employees with filter", tags = "Employee")
    public EmployeeDto[] getEmployees(FilterDto filter) {
        Filter<Employee> employeeFilter = FilterUtils.parseEmployeeFilter(filter);;
        Employee[] employees = storage.getArray(employeeFilter);
        return Arrays.stream(employees).map(
                        employee -> mapperBase.getEmployeeMapper()
                                .toDto(employee, employee.getPost().getId().toString()))
                .toArray(EmployeeDto[]::new);
    }

    @PostMapping("/{id}/set")
    @Operation(summary = "Replace employee, id must exist.", tags = "Employee")
    public ObjectNode setEmployee(@PathVariable UUID id,@RequestBody EmployeeNoIdDto employeeDto) {
        Employee employee = mapperBase.getEmployeeMapper().toEntity(
                employeeDto,
                getPostAction.execute(employeeDto.getPostId()),
                id);
        storage.set(employee);
        return objectMapper.createObjectNode().put("id", employee.getId().toString());
    }

    @PostMapping("/add")
    @Operation(summary = "Add new employee", tags = "Employee")
    public ObjectNode addEmployee(@RequestBody EmployeeNoIdDto employeeNoIdDto) {
        Employee employee = mapperBase.getEmployeeMapper().toEntity(
                employeeNoIdDto,
                getPostAction.execute(employeeNoIdDto.getPostId()),
                UUID.randomUUID());
        storage.add(employee);
        return objectMapper.createObjectNode().put("id", employee.getId().toString());
    }

    @PostMapping("/remove")
    @Operation(summary = "Remove employee with filter", tags = "Employee")
    public ObjectNode removeEmployee(@RequestBody FilterDto filter) {
        filter.isStrictly = true;
        Filter<Employee> employeeFilter = FilterUtils.parseEmployeeFilter(filter);
        return objectMapper.createObjectNode().put("status", storage.remove(employeeFilter));
    }

}
