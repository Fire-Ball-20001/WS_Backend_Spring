package org.backend.spring.controllers;

import org.backend.spring.exceptions.ExcepResponseType;
import org.backend.spring.exceptions.ExceptionResponse;
import org.backend.spring.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ExceptionResponse> notFoundError(NotFoundException ex, WebRequest request)
    {
        ExceptionResponse error = new ExceptionResponse(ex.getMessage(), ExcepResponseType.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
