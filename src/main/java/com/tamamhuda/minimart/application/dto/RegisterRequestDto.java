package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
    private String username;

    @JsonProperty("full_name")
    @NotEmpty(message = "full_name is required")
    private String fullName;

    @Size(min = 8, message = "password must be at least 8 character")
    @NotEmpty(message = "password is required")
    private String password;

    @Email(message = "must be valid email")
    @NotEmpty(message = "email is required")
    private String email;

    @JsonProperty("confirm_password")
    @Size(min = 8, message = "password must be at least 8 character")
    @NotEmpty(message = "confirm_password is required")
    private String confirmPassword;
}
