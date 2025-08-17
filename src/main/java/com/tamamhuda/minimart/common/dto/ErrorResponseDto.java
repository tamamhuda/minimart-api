package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Builder
@JsonPropertyOrder({"status", "error", "message", "path", "timestamp"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "ErrorResponseSchema", description = "Standard API error response")
public class ErrorResponseDto {
    @Schema(description = "Indicates success or failure", example = "401")
    private int status;

    @Schema(description = "Message of error", example = "Invalid or expired token")
    private String message;

    @Schema(description = "Error of the response", example = "2025-08-16T02:00:00Z")
    private Object error;

    @Schema(description = "Path of request", example = "/api/v1/../.")
    private String path;

    @Builder.Default
    @Schema(description = "Timestamp of the response", example = "2025-08-16T02:00:00Z")
    private Instant timestamp = Instant.now();
}
