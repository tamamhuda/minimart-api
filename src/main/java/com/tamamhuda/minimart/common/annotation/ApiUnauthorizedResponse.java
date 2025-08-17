package com.tamamhuda.minimart.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiUnauthorizedResponse {
    String message() default "Invalid or expired authentication token";
    String description() default "Unauthorized – missing or invalid authentication token";
}
