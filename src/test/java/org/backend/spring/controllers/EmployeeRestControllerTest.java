package org.backend.spring.controllers;

import org.assertj.core.util.Lists;
import org.backend.spring.controllers.dto.FilterDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @ParameterizedTest
    void name() {
        //Arrange
        //Act
        //Assert
    }

    private List<Arguments> getFilters() {
        return Lists.newArrayList(
                Arguments.of(FilterDto.builder()
                        .firstName("Test")
                        .isStrictly(true)
                        .build()),
                Arguments.of(FilterDto.builder()
                        .lastName("Test_2")
                        .isStrictly(true)
                        .build()),
                Arguments.of(FilterDto.builder()
                        .postId("Test")
                        .isStrictly(true)
                        .build())
        );
    }


}