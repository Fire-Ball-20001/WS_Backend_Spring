package org.backend.spring.utils;

import org.backend.spring.actions.filters.Filter;
import org.backend.spring.controllers.dto.FilterDto;
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

    public static Filter<PostEmployee> parsePostFilter(FilterDto dto) {
        Filter<PostEmployee> filter = new Filter<>();
        BiFunction<String, String, Boolean> compareFunction;
        if (dto.isStrictly != null && dto.isStrictly) {
            compareFunction = FilterUtils::isStrictlyMatch;
        } else {
            compareFunction = FilterUtils::isApproximatelyMatch;
        }
        if (dto.getPostName() != null) {
            filter.addOperation(
                    (PostEmployee post) -> compareFunction.apply(post.getName(), dto.getPostName()));
        }
        if (dto.getPostId() != null) {
            filter.addOperation(
                    (PostEmployee post) -> compareFunction.apply(post.getId().toString(), dto.getPostId()));
        }
        return filter;
    }

    public static Filter<Contacts> parseContactsFilter(FilterDto dto) {
        Filter<Contacts> filter = new Filter<>();
        BiFunction<String, String, Boolean> compareFunction;
        if (dto.isStrictly != null && dto.isStrictly) {
            compareFunction = FilterUtils::isStrictlyMatch;
        } else {
            compareFunction = FilterUtils::isApproximatelyMatch;
        }
        if (dto.getPhone() != null) {
            filter.addOperation(
                    (Contacts contacts) -> compareFunction.apply(contacts.getPhone(), dto.getPhone()));
        }
        if (dto.getEmail() != null) {
            filter.addOperation(
                    (Contacts contacts) -> compareFunction.apply(contacts.getEmail(), dto.getEmail()));
        }
        if (dto.getWorkEmail() != null) {
            filter.addOperation(
                    (Contacts contacts) -> compareFunction.apply(contacts.getWorkEmail(), dto.getWorkEmail()));
        }
        return filter;
    }

    public static Filter<Employee> parseEmployeeFilter(FilterDto dto) {
        Filter<Employee> filter = new Filter<>();
        BiFunction<String, String, Boolean> compareFunction;
        if (dto.isStrictly != null && dto.isStrictly) {
            compareFunction = FilterUtils::isStrictlyMatch;
        } else {
            compareFunction = FilterUtils::isApproximatelyMatch;
        }
        if (dto.getFirstName() != null) {
            filter.addOperation(
                    (Employee employee) -> compareFunction.apply(employee.getFirstName(), dto.getFirstName()));
        }
        if (dto.getLastName() != null) {
            filter.addOperation(
                    (Employee employee) -> compareFunction.apply(employee.getLastName(), dto.getLastName()));
        }
        if (dto.getId() != null) {
            filter.addOperation(
                    (Employee employee) -> compareFunction.apply(employee.getId().toString(), dto.getId()));
        }
        if (dto.getCharacteristics() != null) {
            filter.addOperation(
                    (Employee employee) -> compareStringArray(compareFunction,employee.getCharacteristics(),dto.getCharacteristics()));
        }
        if (dto.getDescription() != null) {
            filter.addOperation(
                    (Employee employee) -> compareFunction.apply(employee.getDescription(), dto.getDescription()));
        }
        if (dto.getJobType() != null) {
            filter.addOperation(
                    (Employee employee) -> compareFunction.apply(employee.getJobType().toString(), dto.getJobType().toString()));
        }
        filter.addOperation(
                (Employee employee) -> parsePostFilter(dto).match(employee.getPost()));
        filter.addOperation(
                (Employee employee) -> parseContactsFilter(dto).match(employee.getContacts()));
        return filter;
    }
}
