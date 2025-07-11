package com.dgramada.registrationexample.exceptionhandling;

import com.dgramada.registrationexample.exceptionhandling.exception.ResourceAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(Exception ex, WebRequest request) {
        logger.error("An unexpected error occurred: ", ex);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false).replace("uri=", "")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Invalid method arguments: ", ex);
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) ->
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        final var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                "Invalid data provided. Please check the request.",
                request.getDescription(false).replace("uri=", "")
        );
        errorResponse.setValidationErrors(validationErrors);

        return errorResponse;
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException ex, WebRequest request) {
        logger.warn("Resource already exists: ", ex);
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
    }

}
