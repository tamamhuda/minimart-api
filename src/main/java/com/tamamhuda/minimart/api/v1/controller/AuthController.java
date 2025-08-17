package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.schema.ApiResponseRefreshTokenSchema;
import com.tamamhuda.minimart.application.schema.ApiResponseTokenSchema;
import com.tamamhuda.minimart.application.service.impl.AuthServiceImpl;
import com.tamamhuda.minimart.common.annotation.*;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.common.validation.group.Create;
import com.tamamhuda.minimart.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Authentication Management",
        description = "Endpoint to provide authentication for user"
)
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    @Operation(
            summary = "Login to get access and refresh token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseTokenSchema.class)

                            )
                    )
            }
    )
    @ApiValidationErrorResponse()
    @ApiUnauthorizedResponse(message = "Invalid Credentials", description = "Invalid Credentials")
    @ApiNotFoundResponse(description = "User not found", message = "Email not registered")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequestDto request) throws UnauthorizedException {
        TokenDto response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseTokenSchema.class)
                            )
                    )
            }
    )
    @ApiErrorResponseCustomizer(status = "409", message = "Username already taken", description = "Conflict values of the request")
    @ApiValidationErrorResponse()
    public ResponseEntity<TokenDto> register(@Validated(Create.class) @RequestBody UserRequestDto request) {
        TokenDto response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseRefreshTokenSchema.class)

                            )
                    )
            }
    )
    @ApiValidationErrorResponse()
    @ApiUnauthorizedResponse()
    public ResponseEntity<RefreshTokenDto> refresh(@Valid @RequestBody TokenRequest request) {
        RefreshTokenDto response = authService.refresh(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout to revoke token",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @ApiUnauthorizedResponse()
    @ApiValidationErrorResponse()
    public ResponseEntity<?>  logout(@Valid @RequestBody TokenRequest request) {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/resend-verification")
    @PreAuthorize("hasRole('CUSTOMER')")
    @RequiredRoles({"CUSTOMER"})
    @SecurityRequirement(name = "AuthorizationHeader")
    @Operation(
            summary = "Resend email verification",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(
                                            value = "Ok"
                                    )
                            )
                    )
            }
    )
    @ApiValidationErrorResponse()
    @ApiUnauthorizedResponse()
    @ApiErrorResponseCustomizer(status = "400", description = "User already verified", message = "User already verified")
    public ResponseEntity<String> resendVerification(@CurrentUser User user) {
        String response = authService.resendVerification(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/verify")
    @Operation(
            summary = "Received otp token to verify user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Response",
                            content = @Content(
                                    mediaType = "text/html"
                            )
                    )
            }
    )
    @ApiValidationErrorResponse()
    public ModelAndView verify(@RequestParam("token") String token, Model model) {
        VerifyDto verified = authService.verify(token);

        model.addAttribute("message", verified.getMessage());
        model.addAttribute("status", verified.getStatus().name());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("otp-verification");
        return modelAndView;
    }

}
