package com.tamamhuda.minimart.common.advice;

import com.tamamhuda.minimart.common.dto.ApiResponseDto;
import com.tamamhuda.minimart.common.dto.ErrorResponseDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
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



        String path = request.getURI().getPath();

        if (path.contains("swagger") || path.contains("api-docs") || path.contains("healthz") ) {
            return body; // skip wrapping for all swagger endpoints
        }

        // Skip HTML, JSON string, and errors
        if (MediaType.TEXT_HTML.includes(selectedContentType)
                || body instanceof String
                || body instanceof ErrorResponseDto) {
            return body;
        }

        // Skip binary responses (byte arrays)
        if (ByteArrayHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            return body;
        }

        // Wrap normal responses
        Number status = ((ServletServerHttpResponse) response)
                .getServletResponse()
                .getStatus();

        return ApiResponseDto.of(true, status, body);

    }

}
