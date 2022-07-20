package org.backend.spring.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExceptionResponse {
    private String message = "";
    private ExcepResponseType type = ExcepResponseType.UNKNOWN;

    public ExceptionResponse(ExcepResponseType type) {
        this.type = type;
    }

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
