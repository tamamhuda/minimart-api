package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Builder
@JsonPropertyOrder({"status", "error", "message", "path", "timestamp"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private int status;
    private String message;
    private Object error; // Can be String or Map<String, String>
    private String path;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
