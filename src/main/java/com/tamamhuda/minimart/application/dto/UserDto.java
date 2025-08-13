package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({"id", "full_name", "username", "email", "role", "is_verified", "profile_image"})
public class UserDto {

    private String id;

    private String username;

    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("is_verified")
    private boolean isVerified;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("role")
    private String roles;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}
