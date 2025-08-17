package com.tamamhuda.minimart.config;

import com.tamamhuda.minimart.common.annotation.ApiErrorResponseCustomizer;
import com.tamamhuda.minimart.common.annotation.ApiNotFoundResponse;
import com.tamamhuda.minimart.common.annotation.ApiUnauthorizedResponse;
import com.tamamhuda.minimart.common.annotation.ApiValidationErrorResponse;
import com.tamamhuda.minimart.common.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


import java.util.Collections;
import java.util.Map;
import java.util.Set;

@OpenAPIDefinition(
        info = @Info(
                title = "MiniMart-API",
                description = "Complete Spring Boot backend for a MiniMart e-commerce app.",
                contact = @Contact(
                        name = "Tamam Huda",
                        email = "contact@utadev.sh",
                        url = "https://github.com/tamamhuda"
                ),
                license = @License(
                        name = "MIT LICENSE",
                        url = "https://github.com/tamamhuda/minimart-api/blob/main/LICENSE"
                ),
                version = "v1"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8080/api/v1"
                ),
                @Server(
                        description = "Prod Env",
                        url = "https://minimart.utadev.app/api/v1"
                )
        }
)
@SecurityScheme(
        name = "AuthorizationHeader",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER

)
@Configuration
public class OpenApiConfig {


    private final RequestMappingHandlerMapping handlerMapping;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public OpenApiConfig(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI();
    }

    @Bean
    public OpenApiCustomizer apiErrorResponseCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperationsMap().forEach((_, operation) -> handlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethod) -> {
                    Set<String> patterns = mappingInfo.getPathPatternsCondition() != null
                            ? mappingInfo.getPathPatternsCondition().getPatternValues()
                            : Collections.emptySet();

                    if (!patterns.contains(path)) return;

                    ApiErrorResponseCustomizer annotation = handlerMethod.getMethodAnnotation(ApiErrorResponseCustomizer.class);
                    if (annotation == null) return;

                    String message = annotation.message();
                    String description = annotation.description();
                    String status = annotation.status();


                    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                            .status(Integer.parseInt(status))
                            .message(message)
                            .path(contextPath + path)
                            .error(HttpStatus.valueOf(Integer.parseInt(status)).getReasonPhrase())
                            .build();

                    ApiResponse errorResponse = new ApiResponse()
                            .description(description)
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().example(errorResponseDto)
                            ));

                    operation.getResponses().addApiResponse(status, errorResponse);
                }))
        );
    }

    @Bean
    public OpenApiCustomizer apiUnauthorizedCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperationsMap().forEach((_, operation) -> handlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethod) -> {
                    Set<String> patterns = mappingInfo.getPathPatternsCondition() != null
                            ? mappingInfo.getPathPatternsCondition().getPatternValues()
                            : Collections.emptySet();

                    if (!patterns.contains(path)) return;

                    ApiUnauthorizedResponse annotation = handlerMethod.getMethodAnnotation(ApiUnauthorizedResponse.class);
                    if (annotation == null) return;

                    String message = annotation.message();
                    String description = annotation.description();

                    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                            .status(401)
                            .message(message)
                            .path(contextPath + path)
                            .error("Unauthorized")
                            .build();

                    ApiResponse errorResponse = new ApiResponse()
                            .description(description)
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().example(errorResponseDto)
                            ));

                    operation.getResponses().addApiResponse("401", errorResponse);
                }))
        );
    }

    @Bean
    public OpenApiCustomizer apiNotFoundCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperationsMap().forEach((_, operation) -> handlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethod) -> {
                    Set<String> patterns = mappingInfo.getPathPatternsCondition() != null
                            ? mappingInfo.getPathPatternsCondition().getPatternValues()
                            : Collections.emptySet();

                    if (!patterns.contains(path)) return;

                    ApiNotFoundResponse annotation = handlerMethod.getMethodAnnotation(ApiNotFoundResponse.class);
                    if (annotation == null) return;

                    String message = annotation.message();
                    String description = annotation.description();

                    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                            .status(404)
                            .message(message)
                            .path(contextPath + path)
                            .error("Not Found")
                            .build();

                    ApiResponse errorResponse = new ApiResponse()
                            .description(description)
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().example(errorResponseDto)
                            ));

                    operation.getResponses().addApiResponse("404", errorResponse);
                }))
        );
    }

    @Bean
    public OpenApiCustomizer apiValidationErrorCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperationsMap().forEach((_, operation) -> handlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethod) -> {
                    Set<String> patterns = mappingInfo.getPathPatternsCondition() != null
                            ? mappingInfo.getPathPatternsCondition().getPatternValues()
                            : Collections.emptySet();

                    if (!patterns.contains(path)) return;

                    ApiValidationErrorResponse annotation = handlerMethod.getMethodAnnotation(ApiValidationErrorResponse.class);
                    if (annotation == null) return;

                    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                            .status(404)
                            .message("Validation Error")
                            .path(contextPath + path)
                            .error(Map.of(
                                    "field", "field of body or path or query error message"
                            ))
                            .build();

                    ApiResponse errorResponse = new ApiResponse()
                            .description("Validation Error")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().example(errorResponseDto)
                            ));

                    operation.getResponses().addApiResponse("400", errorResponse);
                }))
        );
    }
}