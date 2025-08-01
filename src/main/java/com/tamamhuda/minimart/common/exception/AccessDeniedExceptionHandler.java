package com.tamamhuda.minimart.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.application.dto.ErrorResponseDto;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;


@Component
@AllArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String[] requiredRoles = getRequiredRoles(request);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .statusCode(response.getStatus())
                .message("Access Denied. Required roles: " + String.join(", ", requiredRoles))
                .error(accessDeniedException.getLocalizedMessage())
                .path(request.getRequestURI())
                .build();

        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }

    private String[] getRequiredRoles(HttpServletRequest request) {
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handlerMethod != null) {
            RequiredRoles annotation = handlerMethod.getMethodAnnotation(RequiredRoles.class);
            if (annotation == null) {
                annotation = handlerMethod.getBeanType().getAnnotation(RequiredRoles.class);
            }
            if (annotation != null) {
                return annotation.value();
            }
        }
        return new String[0];
    }
}
