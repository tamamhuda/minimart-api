package com.tamamhuda.minimart.common.exception;


import com.tamamhuda.minimart.common.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.*;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        ErrorResponseDto response = ErrorResponseDto
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation Error")
                .error(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> {
                            String fullPath = violation.getPropertyPath().toString();
                            // Get only the last node in the path
                            String[] pathParts = fullPath.split("\\.");
                            return pathParts[pathParts.length - 1];
                        },
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        ErrorResponseDto response = ErrorResponseDto
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation Error")
                .error(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        Map<String, String> errors = ex.getAllErrors().stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        error -> {
                            Object arg = Objects.requireNonNull(error.getArguments())[0];
                            return (arg instanceof DefaultMessageSourceResolvable resolvable)
                                    ? resolvable.getCode()
                                    : "parameter";
                        },
                        MessageSourceResolvable::getDefaultMessage, (existing, replacement) -> existing, LinkedHashMap::new)
                );

        ErrorResponseDto response = ErrorResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation Error")
                .error(errors)// You must set this in your builder
                .build();

        return ResponseEntity.badRequest().body(response);
    }

}
