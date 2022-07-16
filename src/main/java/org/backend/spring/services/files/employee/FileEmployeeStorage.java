package org.backend.spring.services.files.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.backend.spring.actions.Action;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.controllers.dto.employee.EmployeeDto;
import org.backend.spring.events.BinaryEvent;
import org.backend.spring.exceptions.NotFoundException;
import org.backend.spring.controllers.mappers.EmployeeMapper;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.backend.spring.services.DataStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

import static org.backend.spring.utils.Files.deleteFileOrDirectory;

@ConditionalOnProperty(prefix = "data", name = "source", havingValue = "files", matchIfMissing = true)
@Component
@CommonsLog
@RequiredArgsConstructor
public class FileEmployeeStorage implements DataStorage<Employee> {
    private Map<UUID, Employee> employees = new HashMap<>();
    @Value("${data.path-to-employees}")
    private final String pathStr;
    private final ObjectMapper objectMapper;
    private final EmployeeMapper employeeMapper;
    private final Action<String,PostEmployee> getPost;
    private final BinaryEvent<PostEmployee, PostEmployee> event;


    @PostConstruct
    private void postConstructor() {
        try {
            loadData();
        } catch (RuntimeException e) {
            log.error("Error load employees data");
        }
        event.addListener(this::updateEmployees);
    }

    @Override
    public Employee get(Filter<Employee> filter) {
        Optional<Employee> tempPostEmployee = employees.values().stream()
                .filter(filter::match)
                .findFirst();
        if (!tempPostEmployee.isPresent()) {
            throw new NotFoundException("Not found employee object.");
        }
        return tempPostEmployee.get();
    }

    @Override
    public Optional<Employee> getOptional(Filter<Employee> filter) {
        return employees.values().stream()
                .filter(filter::match)
                .findFirst();
    }

    @Override
    public void set(Employee argument) {

        if (!employees.containsKey(argument.getId())) {
            throw new NotFoundException("Not found employee object");
        }
        employees.replace(argument.getId(), argument);
        saveData();
    }

    public Employee[] getArray(Filter<Employee> filter) {
        return employees.values().stream().filter(filter::match)
                .toArray(Employee[]::new);
    }

    @Override
    public void add(Employee argument) {
        employees.put(argument.getId(), argument);
        saveData();
    }

    @Override
    public boolean remove(Filter<Employee> filter) {
        if (employees.values().stream().noneMatch(filter::match)) {
            throw new NotFoundException("Not found employee object.");
        }
        for (Employee employee :
                employees.values().toArray(new Employee[0])) {
            if (filter.match(employee)) {
                employees.remove(employee.getId());
            }
        }
        saveData();
        return true;
    }

    private Void updateEmployees(PostEmployee old, PostEmployee newPost) {
        Employee[] employees = getArray(
                new Filter<Employee>().addOperation(
                        (Employee employee) -> employee.getPost().getId().equals(old.getId())));
        for (Employee employee : employees) {
            employee.setPost(newPost);
            set(employee);
        }
        return null;
    }


    private void loadData() {
        String employees_string;
        EmployeeDto[] employees;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(pathStr))) {
            employees_string = fileReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Error loading data");
        }

        try {
            employees = objectMapper.readValue(employees_string, EmployeeDto[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error loading data");
        }
        this.employees = new HashMap<>();
        for (EmployeeDto dto : employees) {
            Employee employee = employeeMapper.toEntity(dto);
            employee.setPost(getPost.execute(employee.getPost().getId().toString()));
            this.employees.put(employee.getId(), employee);
        }
    }

    private void saveData() {
        List<EmployeeDto> employees = new ArrayList<>();
        if (!deleteFileOrDirectory(new File(pathStr))) {
            throw new RuntimeException("Error save employees data");
        }
        if (this.employees.size() == 0) {
            return;
        }
        for (Employee object : this.employees.values()) {
            employees.add(employeeMapper.toDto(object));
        }
        try (FileWriter fw = new FileWriter(pathStr)) {
            fw.write(
                    objectMapper.writeValueAsString(employees.toArray(new EmployeeDto[0])));
            fw.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error save employees data ", e);
        }
    }

}
