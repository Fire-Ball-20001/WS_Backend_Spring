package org.backend.spring.utils;

import org.backend.spring.actions.filters.Filter;
import org.backend.spring.controllers.dto.filter.ContactsFilterDto;
import org.backend.spring.controllers.dto.filter.EmployeeFilterDto;
import org.backend.spring.controllers.dto.filter.PostFilterDto;
import org.backend.spring.models.Contacts;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FilterUtils {
    public static boolean isApproximatelyMatch(String orig, String find) {
        StringBuilder find_regex = new StringBuilder(".*");
        for (char ch : find.toCharArray()) {
            find_regex.append(ch).append(".*");
        }
        return orig.matches(find_regex.toString());
    }

    public static boolean isStrictlyMatch(String orig, String find) {
        return orig.equals(find);
    }

    public static boolean compareStringArray(BiFunction<String, String, Boolean> compare, String[] first, String[] second) {
        List<String> firstList = Arrays.stream(first).sorted().collect(Collectors.toList());
        List<String> secondList = Arrays.stream(second).sorted().collect(Collectors.toList());
        return firstList.stream().allMatch((String firstEl) -> {
            for (String secondEl : secondList) {
                if (compare.apply(firstEl, secondEl)) {
                    return true;
                }
            }
            return false;
        });
    }

    private static boolean check(String first, String second) {
        if (first == null || second == null) {
            return true;
        }
        return isApproximatelyMatch(first, second);
    }

    public static Filter<PostEmployee> parsePostFilter(PostFilterDto dto) {
        Filter<PostEmployee> filter = new Filter<>();
        if (dto == null) {
            return filter;
        }
        filter.addOperation(
                (PostEmployee post) -> check(post.getName(), dto.getName()));
        filter.addOperation(
                (PostEmployee post) -> check(post.getId().toString(), dto.getId()));

        return filter;
    }

    public static Filter<Contacts> parseContactsFilter(ContactsFilterDto dto) {
        Filter<Contacts> filter = new Filter<>();
        if (dto == null) {
            return filter;
        }
        filter.addOperation(
                (Contacts contacts) -> check(contacts.getPhone(), dto.getPhone()));
        filter.addOperation(
                (Contacts contacts) -> check(contacts.getEmail(), dto.getEmail()));
        filter.addOperation(
                (Contacts contacts) -> check(contacts.getWorkEmail(), dto.getWorkEmail()));

        return filter;
    }

    public static Filter<Employee> parseEmployeeFilter(EmployeeFilterDto dto) {
        Filter<Employee> filter = new Filter<>();
        if (dto == null) {
            return filter;
        }

        filter.addOperation(
                (Employee employee) -> check(employee.getFirstName(), dto.getFirstName()));
        filter.addOperation(
                (Employee employee) -> check(employee.getLastName(), dto.getLastName()));
        filter.addOperation(
                (Employee employee) -> check(employee.getId().toString(), dto.getId()));
        filter.addOperation(
                (Employee employee) -> compareStringArray(FilterUtils::check, employee.getCharacteristics(), dto.getCharacteristics()));
        filter.addOperation(
                (Employee employee) -> check(employee.getDescription(), dto.getDescription()));
        filter.addOperation(
                (Employee employee) -> check(employee.getJobType().toString(), dto.getJobType().toString()));
        filter.addOperation(
                (Employee employee) -> parsePostFilter(dto.getPost()).match(employee.getPost()));
        filter.addOperation(
                (Employee employee) -> parseContactsFilter(dto.getContacts()).match(employee.getContacts()));
        return filter;
    }
}
