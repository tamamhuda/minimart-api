package com.tamamhuda.minimart.common.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    private static final String DEFAULT_MESSAGE = "Access denied. Unauthorized.";

    public UnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UnauthorizedException(String message) {
        super(message != null && !message.isBlank() ? message : DEFAULT_MESSAGE);
    }
}
