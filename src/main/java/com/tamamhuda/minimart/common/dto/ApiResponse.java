package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@JsonPropertyOrder({"success", "status",  "data", "timestamp"})
public class ApiResponse {
    private boolean success;
    private Number status;
    private Object data;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public static ApiResponse of(boolean success, Number status, Object data) {
        return new ApiResponse(success, status, data, Instant.now());
    }

}
