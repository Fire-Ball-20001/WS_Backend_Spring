package org.backend.spring.controllers.employeeController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.controllers.dto.filter.EmployeeFilterDto;
import org.backend.spring.controllers.dto.employee.EmployeeDto;
import org.backend.spring.controllers.dto.employee.EmployeeNoIdUsePostIdDto;
import org.backend.spring.controllers.mappers.EmployeeMapper;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataService;
import org.backend.spring.utils.FilterUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeRestController {

    DataService<Employee> storage;
    ObjectMapper objectMapper;
    EmployeeMapper mapper;
    Action<String, PostEmployee> getPostAction;

    @GetMapping("/{id}")
    @Operation(summary = "Get one employee", tags = "Employee")
    public EmployeeDto getEmployee(@PathVariable UUID id) {
        Filter<Employee> employeeFilter = new Filter<>();
        employeeFilter.addOperation((employee -> FilterUtils.isStrictlyMatch(id.toString(),employee.getId().toString())));
        Employee find = storage.get(employeeFilter);
        return mapper.toDto(find);
    }

    @GetMapping("/list")
    @Operation(summary = "Get more employees with filter", tags = "Employee")
    public EmployeeDto[] getEmployees(EmployeeFilterDto filter) {
        Filter<Employee> employeeFilter = FilterUtils.parseEmployeeFilter(filter);;
        Employee[] employees = storage.getArray(employeeFilter);
        return Arrays.stream(employees).map(
                        employee -> mapper
                                .toDto(employee))
                .toArray(EmployeeDto[]::new);
    }

    @PostMapping("/{id}/set")
    @Operation(summary = "Replace employee", tags = "Employee")
    public EmployeeDto setEmployee(@PathVariable UUID id,@RequestBody EmployeeNoIdUsePostIdDto employeeDto) {
        Employee employee = mapper.toEntity(
                employeeDto,
                getPostAction.execute(employeeDto.getPostId()),
                id);
        storage.set(employee);
        return mapper.toDto(employee);
    }

    @PostMapping("/create")
    @Operation(summary = "Create new employee", tags = "Employee")
    public EmployeeDto createEmployee(@RequestBody EmployeeNoIdUsePostIdDto employeeNoIdDto) {
        Employee employee = mapper.toEntity(
                employeeNoIdDto,
                getPostAction.execute(employeeNoIdDto.getPostId()),
                UUID.randomUUID());
        storage.add(employee);
        return mapper.toDto(employee);
    }

    @PostMapping("/remove")
    @Operation(summary = "Remove employee with filter", tags = "Employee")
    public void removeEmployee(@RequestBody EmployeeFilterDto filter) {
        Filter<Employee> employeeFilter = FilterUtils.parseEmployeeFilter(filter);
        storage.remove(employeeFilter);
    }

}
