package org.backend.spring.services.utils;

public class BooleanUtils {
    public static boolean isApproximatelyMatch(String orig, String find) {
        StringBuilder find_regex = new StringBuilder(".*");
        for (char ch : find.toCharArray()) {
            find_regex.append(ch).append(".*");
        }
        return orig.matches(find_regex.toString());
    }
}
