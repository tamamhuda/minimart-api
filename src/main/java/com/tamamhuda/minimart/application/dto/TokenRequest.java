package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TokenRequest {

    @NotEmpty(message = "token is required")
    @NotBlank(message = "token cannot be blank")
    @JsonProperty("token")
    private String token;
}
