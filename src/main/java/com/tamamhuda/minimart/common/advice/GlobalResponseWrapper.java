package com.tamamhuda.minimart.common.advice;

import com.tamamhuda.minimart.common.dto.ApiResponse;
import com.tamamhuda.minimart.common.dto.ErrorResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(@NonNull  MethodParameter returnType,
                            @NonNull  Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull  MediaType selectedContentType,
                                  @NonNull  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull  ServerHttpRequest request,
                                  @NonNull  ServerHttpResponse response) {

        if (body instanceof String) {
            return body; // skip wrapping
        }

        if (MediaType.TEXT_HTML.includes(selectedContentType)
               && !selectedContentType.includes(MediaType.APPLICATION_JSON)) {
            return body;
        }

        if (body instanceof ErrorResponse) {
            return body;
        }

        Number status = HttpStatus.valueOf(((ServletServerHttpResponse) response).getServletResponse().getStatus()).value();

        return ApiResponse.of(true, status, body);

    }

}
