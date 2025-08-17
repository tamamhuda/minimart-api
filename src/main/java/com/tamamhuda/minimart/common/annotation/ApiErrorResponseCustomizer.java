package com.tamamhuda.minimart.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiErrorResponseCustomizer {
    String message() default "";
    String description() default "";
    String status() default "500";

}
