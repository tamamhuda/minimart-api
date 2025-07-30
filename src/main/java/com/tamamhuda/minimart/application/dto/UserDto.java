package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({"id", "full_name", "username", "email", "role", "is_verified"})
public class UserDto {

    private String id;

    private String username;

    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("is_verified")
    private boolean isVerified;

    @JsonProperty("role")
    private String roles;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}
