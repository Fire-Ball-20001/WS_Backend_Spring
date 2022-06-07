package org.backend.spring.services.utils;

import org.backend.spring.models.PostEmployee;

import java.util.UUID;

public class PostUtils {
    public static PostEmployee getDefaultPost()
    {
        return PostEmployee.builder()
                .id(UUID.fromString("0000000-0000-0000-0000-000000000000"))
                .name("Нет должности")
                .build();
    }
}
