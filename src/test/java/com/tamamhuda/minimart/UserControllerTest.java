package com.tamamhuda.minimart;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamamhuda.minimart.api.v1.controller.AuthController;
import com.tamamhuda.minimart.api.v1.controller.UserController;
import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.application.dto.UserRequestChangePassword;
import com.tamamhuda.minimart.application.dto.UserRequestDto;
import com.tamamhuda.minimart.application.mapper.UserMapper;
import com.tamamhuda.minimart.application.service.impl.JwtServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserServiceImpl;
import com.tamamhuda.minimart.config.DotenvInitializer;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    private UserDto mockUser;

    private UUID userId;

    private final String username = "testuser";

    @MockitoBean
    private JwtServiceImpl jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername(username);
        user.setFullName("Test User");
        user.setPassword("hashedPassword");
        user.setEmail("test@example.com");
        user.setRoles(Role.CUSTOMER);
        user.setVerified(true);
        user.setEnabled(true);
        user.setId(UUID.randomUUID());

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // Prepare mock user
        userId = user.getId();
        mockUser = userMapper.toDto(user);
    }


    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    private UserDetails mockAuthenticatedUser(Role role) {
        Mockito.when(jwtService.extractUsername(anyString())).thenReturn("testuser");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("password")
                .roles(role.name())
                .build();


        Mockito.when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        Mockito.when(jwtService.isAccessTokenValid("dummy-access-token", userDetails)).thenReturn(true);

        return userDetails;
    }



    private RequestPostProcessor authorizationToken(UserDetails userDetails) {
        return request -> {
            request.addHeader("Authorization", "Bearer dummy-access-token");
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return request;
        };
    }

    @Test
    @DisplayName("GET /users/me should return authenticated user")
    void testGetMe() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        Mockito.when(userService.me(any())).thenReturn(mockUser);

        mockMvc.perform(get("/users/me")
                        .with(authorizationToken(customerDetails))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId.toString()))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    @DisplayName("PUT /users/{id} should update user")
    void testUpdateUser() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);

        User user = userMapper.toEntity(mockUser);
        user.setFullName("Full Name Updated");

        Mockito.when(userService.update(any(UserRequestDto.class), eq(userId)))
                .thenReturn(userMapper.toDto(user));

        UserRequestDto requestBody =UserRequestDto.builder()
                .fullName("Full Name Updated")
                .build();

        mockMvc.perform(put("/users/" + userId)
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.full_name").value("Full Name Updated"));
    }

    @Test
    @DisplayName("PUT /users/{id}/change-password should change user password")
    void testChangePassword() throws Exception {
        UserDetails customerDetails = mockAuthenticatedUser(Role.CUSTOMER);


        Mockito.when(userService.changePassword(any(UserRequestChangePassword.class), eq(userId)))
                .thenReturn(mockUser);

        UserRequestChangePassword requestBody = UserRequestChangePassword.builder()
                .oldPassword("hashedPassword")
                .newPassword("newHashedPassword")
                .confirmNewPassword("newHashedPassword")
                .build();

        mockMvc.perform(put("/users/" + userId + "/change-password")
                        .with(authorizationToken(customerDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId.toString()));
    }

    @Test
    @DisplayName("DELETE /users/{id} should delete user")
    void testDeleteUser() throws Exception {
        UserDetails adminDetails = mockAuthenticatedUser(Role.ADMIN);

        mockMvc.perform(delete("/users/" + userId)
                        .with(authorizationToken(adminDetails)))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUserById(userId);
    }
}