package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"statusCode", "error", "message", "path", "timestamp"})
public class ErrorResponse {
    private int statusCode;
    private String message;
    private Object error; // Can be String or Map<String, String>
    private String path;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
