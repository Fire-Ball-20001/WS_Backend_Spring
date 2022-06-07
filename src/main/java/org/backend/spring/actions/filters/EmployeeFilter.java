package org.backend.spring.actions.filters;

import lombok.Builder;
import lombok.Getter;
import org.backend.spring.models.Employee;

import java.util.Arrays;
import java.util.UUID;

@Builder
@Getter
public class EmployeeFilter implements Filter<Employee> {
    private String id;
    private String firstName;
    private String lastName;
    private String description;
    private PostFilter postFilter;
    private String[] characteristics;
    @Builder.Default
    private boolean isStrictly = false;

    @Override
    public boolean match(Employee object) {
        if(isStrictly)
        {
            return matchStrictly(object);
        }
        else
        {
            return matchApproximately(object);
        }
    }

    @Override
    public boolean matchStrictly(Employee object) {
        if (id != null) {
            UUID uuid;
            try {
                uuid = UUID.fromString(id);
            } catch (Exception e) {
                return false;
            }
            if (!object.getId().equals(uuid)) {
                return false;
            }
        }
        if (firstName != null) {
            if (!object.getFirstName().equals(firstName)) {
                return false;
            }
        }
        if (lastName != null) {
            if (!object.getLastName().equals(lastName)) {
                return false;
            }
        }
        if (description != null) {
            if (!object.getDescription().equals(description)) {
                return false;
            }
        }
        if (postFilter != null) {
            if (!postFilter.matchStrictly(object.getPost())) {
                return false;
            }
        }
        if (characteristics != null) {
            if (object.getCharacteristics().length != characteristics.length) {
                return false;
            } else {
                return Arrays.stream(object.getCharacteristics()).allMatch(
                        (String character) ->
                                Arrays.asList(characteristics).contains(character));
            }
        }
        return true;
    }

    @Override
    public boolean matchApproximately(Employee object) {
        if (id != null) {

            if (!isApproximatelyMatch(object.getId().toString(), id)) {
                return false;
            }
        }
        if (firstName != null) {
            if (!isApproximatelyMatch(
                    object.getFirstName(), firstName)) {
                return false;
            }
        }
        if (lastName != null) {
            if (!isApproximatelyMatch(
                    object.getLastName(), lastName)) {
                return false;
            }
        }
        if (description != null) {
            if (!isApproximatelyMatch(
                    object.getDescription(), description)) {
                return false;
            }
        }
        if (postFilter != null) {
            if (!postFilter.matchApproximately(object.getPost())) {
                return false;
            }
        }
        if (characteristics != null) {

            return Arrays.stream(object.getCharacteristics()).allMatch(
                    (String character) ->
                    {
                        for (String str :
                                characteristics) {
                            if (isApproximatelyMatch(character, str)) {
                                return true;
                            }
                        }
                        return false;
                    });

        }
        return true;
    }

    private boolean isApproximatelyMatch(String orig, String find) {
        StringBuilder find_regex = new StringBuilder(".*");
        for (char ch : find.toCharArray()) {
            find_regex.append(ch).append(".*");
        }
        return orig.matches(find_regex.toString());
    }
}
