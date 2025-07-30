package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.LoginRequestDto;
import com.tamamhuda.minimart.application.dto.RegisterRequestDto;
import com.tamamhuda.minimart.application.dto.TokenResponseDto;
import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.application.service.impl.AuthServiceImpl;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
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
    public ResponseEntity<UserDto> me() {
        return authService.me();
    }
}
