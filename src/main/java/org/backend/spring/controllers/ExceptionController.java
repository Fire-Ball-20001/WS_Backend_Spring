package org.backend.spring.controllers;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Not Found Object",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ExceptionResponse.class))
                            )
                    }
            )
    })
    protected ResponseEntity<ExceptionResponse> notFoundError(NotFoundException ex, WebRequest request)
    {
        ExceptionResponse error = new ExceptionResponse(ex.getMessage(), ExcepResponseType.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
