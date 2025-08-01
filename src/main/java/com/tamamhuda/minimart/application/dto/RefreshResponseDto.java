package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshResponseDto {

    @JsonProperty("access_token")
    private String accessToken;
}
