package com.coderbd.controller;


import com.coderbd.exceptions.PriceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);
    private static final String DEFAULT_VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final String DEFAULT_UNSUPPORTED_OP_MESSAGE = "Operation not supported";


    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handlePriceExceptions(
            RuntimeException ex, WebRequest request) {
        log.error("Error in Pricing API - handlePriceExceptions - "+ex.getMessage()+" \n Caused by: "+ex.getCause());
        String bodyOfResponse;
        if ((ex instanceof ResponseStatusException) && (ex.getCause() instanceof PriceException)) {
            bodyOfResponse = "Price Not Found";
            return handleExceptionInternal(ex, bodyOfResponse,
                    new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        } else {
            bodyOfResponse = "Operation not supported.";
            return handleExceptionInternal(ex, bodyOfResponse,
                    new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(
                        Collectors.toList());
        log.error("Error in Pricing API - handleMethodArgumentNotValid - "+errors);
        Map<String, String> apiError = new HashMap<>();
        apiError.put(DEFAULT_VALIDATION_FAILED_MESSAGE, errors.toString());
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }
}