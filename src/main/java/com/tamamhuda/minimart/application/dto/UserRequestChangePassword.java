package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class UserRequestChangePassword {
    @JsonProperty("old_password")
    @NotBlank(message = "old_password is required")
    private String oldPassword;

    @JsonProperty("new_password")
    @NotBlank(message = "new_password is required")
    private String newPassword;

    @JsonProperty("confirm_new_password")
    @NotBlank(message = "confirm_new_password is required")
    private String confirmNewPassword;
}
