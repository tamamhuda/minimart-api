package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamamhuda.minimart.common.validation.group.Update;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.awt.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserRequestDto {

    @NotBlank(message = "username is required", groups = {Insets.class})
    @Size(min = 5, message = "username must be at least 8 character", groups = {Insets.class})
    private String username;

    @JsonProperty("full_name")
    @NotBlank(message = "full_name is required", groups = {Update.class, Insets.class})
    private String fullName;

    @Size(min = 8, message = "password must be at least 8 character", groups = {Insets.class})
    @NotBlank(message = "password is required", groups = {Insets.class})
    private String password;

    @Email(message = "must be valid email", groups = {Insets.class})
    @NotBlank(message = "email is required", groups = {Insets.class})
    private String email;

    @JsonProperty("confirm_password")
    @Size(min = 8, message = "password must be at least 8 character", groups = {Insets.class})
    @NotBlank(message = "confirm_password is required", groups = {Insets.class})
    private String confirmPassword;
}
