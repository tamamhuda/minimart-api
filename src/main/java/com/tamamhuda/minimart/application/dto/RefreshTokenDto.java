package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "RefreshTokenSchema")
public class RefreshTokenDto {

    @JsonProperty("access_token")
    private String accessToken;
}
