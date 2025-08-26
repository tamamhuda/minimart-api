package com.tamamhuda.minimart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.api.v1.controller.AuthController;
import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.service.impl.AuthServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import com.tamamhuda.minimart.config.JwtAuthenticationFilter;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.enums.OtpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private AuthServiceImpl authService;

    @Autowired
    private ObjectMapper objectMapper;

    private TokenDto tokenDto;

    @BeforeEach
    void setup() {
        tokenDto = TokenDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();
    }

    @Test
    @DisplayName("Login success returns token")
    void login_success() throws Exception {
        LoginRequestDto request = LoginRequestDto.builder()
                .username("johndoe")
                .password("password")
                .build();

        Mockito.when(authService.login(any(LoginRequestDto.class))).thenReturn(tokenDto);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.access_token").value("access-token"))
                .andExpect(jsonPath("$.data.refresh_token").value("refresh-token"));
    }

    @Test
    @DisplayName("Register success returns token")
    void register_success() throws Exception {
        UserRequestDto request = UserRequestDto.builder()
                .username("johndoe")
                .fullName("John Doe")
                .email("johndoe@example.com")
                .password("password")
                .confirmPassword("password")
                .build();

        Mockito.when(authService.register(any(UserRequestDto.class))).thenReturn(tokenDto);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.access_token").value("access-token"))
                .andExpect(jsonPath("$.data.refresh_token").value("refresh-token"));
    }

    @Test
    @DisplayName("Refresh token success")
    void refresh_success() throws Exception {
        TokenRequest request = TokenRequest.builder()
                .token("refresh-token")
                .build();

        RefreshTokenDto refreshResponse = RefreshTokenDto.builder()
                .accessToken("new-access-token")
                .build();

        Mockito.when(authService.refresh(any(TokenRequest.class))).thenReturn(refreshResponse);

        mockMvc.perform(post("/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.access_token").value("new-access-token"));
    }

    @Test
    @DisplayName("Logout success returns 204")
    void logout_success() throws Exception {
        TokenRequest request = TokenRequest.builder()
                .token("access-token")
                .build();

        Mockito.doNothing().when(authService).logout(any(TokenRequest.class));

        mockMvc.perform(post("/auth/logout")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Resend verification success")
    void resendVerification_success() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("customer");

        Mockito.when(authService.resendVerification(any(User.class))).thenReturn("OK");

        mockMvc.perform(post("/auth/resend-verification")
                        .requestAttr("user", mockUser)) // simulating @CurrentUser
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Verify success returns HTML")
    void verify_success() throws Exception {
        VerifyDto verifyDto = VerifyDto.builder()
                .status(OtpStatus.SUCCESS)
                .message("Your account has been successfully verified! You can now log in.")
                .build();

        Mockito.when(authService.verify(eq("otp-token"))).thenReturn(verifyDto);

        mockMvc.perform(get("/auth/verify")
                        .param("token", "otp-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("otp-verification"))
                .andExpect(model().attribute("status", "SUCCESS"))
                .andExpect(model().attribute("message", "Your account has been successfully verified! You can now log in."));
    }
}

