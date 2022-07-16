package org.backend.spring.controllers.employeeController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.controllers.dto.FilterDto;
import org.backend.spring.controllers.dto.employee.EmployeeDto;
import org.backend.spring.controllers.dto.employee.EmployeeNoIdDto;
import org.backend.spring.controllers.dto.employee.EmployeeNoIdUsePostIdDto;
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
    @Operation(summary = "Get one employee", tags = "Employee")
    public EmployeeDto getEmployee(@PathVariable UUID id) {
        Filter<Employee> employeeFilter = new Filter<>();
        employeeFilter.addOperation((employee -> FilterUtils.isStrictlyMatch(id.toString(),employee.getId().toString())));
        Employee find = storage.get(employeeFilter);
        return mapperBase.getEmployeeMapper().toDto(find);
    }

    @GetMapping("/list")
    @Operation(summary = "Get more employees with filter", tags = "Employee")
    public EmployeeDto[] getEmployees(FilterDto filter) {
        Filter<Employee> employeeFilter = FilterUtils.parseEmployeeFilter(filter);;
        Employee[] employees = storage.getArray(employeeFilter);
        return Arrays.stream(employees).map(
                        employee -> mapperBase.getEmployeeMapper()
                                .toDto(employee))
                .toArray(EmployeeDto[]::new);
    }

    @PostMapping("/{id}/set")
    @Operation(summary = "Replace employee", tags = "Employee")
    public EmployeeDto setEmployee(@PathVariable UUID id,@RequestBody EmployeeNoIdUsePostIdDto employeeDto) {
        Employee employee = mapperBase.getEmployeeMapper().toEntity(
                employeeDto,
                getPostAction.execute(employeeDto.getPostId()),
                id);
        storage.set(employee);
        return mapperBase.getEmployeeMapper().toDto(employee);
    }

    @PostMapping("/add")
    @Operation(summary = "Add new employee", tags = "Employee")
    public EmployeeDto addEmployee(@RequestBody EmployeeNoIdUsePostIdDto employeeNoIdDto) {
        Employee employee = mapperBase.getEmployeeMapper().toEntity(
                employeeNoIdDto,
                getPostAction.execute(employeeNoIdDto.getPostId()),
                UUID.randomUUID());
        storage.add(employee);
        return mapperBase.getEmployeeMapper().toDto(employee);
    }

    @PostMapping("/remove")
    @Operation(summary = "Remove employee with filter", tags = "Employee")
    public void removeEmployee(@RequestBody FilterDto filter) {
        filter.isStrictly = true;
        Filter<Employee> employeeFilter = FilterUtils.parseEmployeeFilter(filter);
        storage.remove(employeeFilter);
    }

}
