package com.tamamhuda.minimart.common.advice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.common.dto.ErrorResponseDto;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
                .status(HttpStatus.BAD_REQUEST.value())
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

                            String[] pathParts = fullPath.split("\\.");
                            return pathParts[pathParts.length - 1];
                        },
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        ErrorResponseDto response = ErrorResponseDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
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
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation Error")
                .error(errors)// You must set this in your builder
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public void handleResponseStatusException(ResponseStatusException ex,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException {


        HttpStatusCode statusCode = ex.getStatusCode();
        String message = ex.getReason() != null ? ex.getReason() : "Unknown Status";


        response.setStatus(statusCode.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(statusCode.value())
                .message(message)
                .error(statusCode instanceof HttpStatus httpStatus
                        ? httpStatus.getReasonPhrase()
                        : "HTTP " + statusCode.value())
                .path(request.getRequestURI())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex, HttpServletRequest request) {

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message( ex.getMessage())
                .error(ex.getClass().getSimpleName())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .error(ex.getClass().getSimpleName())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);

    }


}
