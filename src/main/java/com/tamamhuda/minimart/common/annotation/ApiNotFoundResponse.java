package com.tamamhuda.minimart.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiNotFoundResponse {
    String message() default "Resource not found";
    String description() default "Not Found";
}
