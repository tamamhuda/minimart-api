package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "TokenRequestSchema")
public class TokenRequest {

    @NotBlank(message = "token is required")
    @JsonProperty("token")
    private String token;
}
