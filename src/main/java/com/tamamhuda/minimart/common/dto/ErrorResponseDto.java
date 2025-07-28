package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"statusCode", "message", "error", "path", "timestamp"})
public class ErrorResponseDto {
    private int statusCode;
    private String message;
    private Map<String, String> error;
    private String path; // nullable
    @Builder.Default
    private Instant timestamp = Instant.now();
}