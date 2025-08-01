package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.service.impl.AuthServiceImpl;
import com.tamamhuda.minimart.common.annotation.CurrentUser;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) throws UnauthorizedException {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@CurrentUser User user) {
        return authService.me(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(@Valid @RequestBody TokenRequest request) {
        return authService.refresh(request);

    }

    @PostMapping("/logout")
    public ResponseEntity<?>  logout(@Valid @RequestBody TokenRequest request) {
        return authService.logout(request);
    }
}
