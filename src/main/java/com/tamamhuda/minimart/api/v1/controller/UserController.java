package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.application.dto.UserRequestChangePassword;
import com.tamamhuda.minimart.application.dto.UserRequestDto;
import com.tamamhuda.minimart.application.mapper.UserMapper;
import com.tamamhuda.minimart.application.schema.ApiResponsePageUserSchema;
import com.tamamhuda.minimart.application.schema.ApiResponseUserSchema;
import com.tamamhuda.minimart.application.service.impl.UserServiceImpl;
import com.tamamhuda.minimart.common.annotation.*;
import com.tamamhuda.minimart.common.dto.ErrorResponseDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.common.validation.group.Update;
import com.tamamhuda.minimart.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.CredentialException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "User Management",
        description = "Manage all user authenticated"
)
@SecurityRequirement(name = "AuthorizationHeader")
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    @Operation(summary = "Get user authenticated",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseUserSchema.class)))
            })
    @ApiUnauthorizedResponse()
    public ResponseEntity<UserDto> me(@CurrentUser User user) {
        UserDto response = userService.me(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{user_id}")
    @Operation(summary = "Update user authenticated",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseUserSchema.class)))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "User not found", message = "User not found")
    public ResponseEntity<UserDto> update(@Validated(Update.class) @RequestBody UserRequestDto request, @PathVariable("user_id") UUID userId) {
        UserDto response = userService.update(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{user_id}/change-password")
    @Operation(summary = "Change password user authenticated",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseUserSchema.class)))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "User not found", message = "User not found")
    public ResponseEntity<UserDto> changePassword(@PathVariable("user_id") UUID userId, @Valid @RequestBody UserRequestChangePassword request) throws CredentialException {
        UserDto response = userService.changePassword(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @Operation(summary = "Get user by Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseUserSchema.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class
                                    )))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "User not found", message = "User not found")
    public ResponseEntity<UserDto> getUserById(@PathVariable("user_id") UUID userId) {
        User user = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toDto(user));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @Operation(summary = "Get all users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponsePageUserSchema.class)))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "User not found", message = "User not found")
    public ResponseEntity<PageDto<UserDto>> getAllUsers(Pageable pageable) {
        PageDto<UserDto> user = userService.getAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    @Operation(summary = "Delete user by Id",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json"
                                   ))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "User not found", message = "User not found")
    public ResponseEntity<?> deleteUserById(@PathVariable("user_id") UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/{user_id}/upload", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload user profile image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseUserSchema.class)))
            })
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    @ApiNotFoundResponse(description = "User not found", message = "User not found")
    public ResponseEntity<UserDto> uploadProfileImage(@PathVariable("user_id") UUID userId, @RequestPart("file") MultipartFile file){
        UserDto response = userService.uploadProfileImage(file, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping(value = "/images/{imageUrl}", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(
            summary = "View profile image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "image/png"

                            )
                    )
            }
    )
    @ApiNotFoundResponse(description = "Image profile not found", message = "Image profile not found")
    public void proxyProductImage(
            @PathVariable("imageUrl") String imageUrl,
            HttpServletResponse response
    ) {
        userService.proxyProfileImage(response, imageUrl);
    }

}
