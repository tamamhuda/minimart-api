package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SessionDto extends BaseDto {

    @JsonProperty("user_id")
    private User user;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("is_session_revoked")
    private Boolean isSessionRevoked;

    @JsonProperty("access_token_expires_at")
    private Instant accessTokenExpiresAt;

    @JsonProperty("refresh_token_expires_at")
    private Instant refreshTokenExpiresAt;

    @JsonProperty("session_revoked_at")
    private Instant sessionRevokedAt;

}
