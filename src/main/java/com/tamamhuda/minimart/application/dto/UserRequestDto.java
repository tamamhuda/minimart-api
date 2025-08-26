package com.tamamhuda.minimart.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.common.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Schema(name = "UserRequestSchema")
public class UserRequestDto {

    @NotBlank(message = "username is required", groups = {Create.class})
    @Size(min = 5, message = "username must be at least 8 character", groups = {Create.class})
    private String username;

    @JsonProperty("full_name")
    @NotBlank(message = "full_name is required", groups = {Update.class, Create.class})
    private String fullName;

    @Size(min = 8, message = "password must be at least 8 character", groups = {Create.class})
    @NotBlank(message = "password is required", groups = {Create.class})
    private String password;

    @Email(message = "must be valid email", groups = {Create.class})
    @NotBlank(message = "email is required", groups = {Create.class})
    private String email;

    @JsonProperty("confirm_password")
    @Size(min = 8, message = "password must be at least 8 character", groups = {Create.class})
    @NotBlank(message = "confirm_password is required", groups = {Create.class})
    private String confirmPassword;
}
