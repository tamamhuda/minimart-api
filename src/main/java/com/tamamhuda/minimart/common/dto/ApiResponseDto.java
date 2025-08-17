package com.tamamhuda.minimart.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "ApiResponseSchema")
@JsonPropertyOrder({"success", "status",  "data", "timestamp"})
public class ApiResponseDto<T> {
    private boolean success;
    private Number status;
    private T data;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public static ApiResponseDto<Object> of(boolean success, Number status, Object data) {
        return new ApiResponseDto<>(success, status, data, Instant.now());
    }

}
