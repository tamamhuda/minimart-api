package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.service.impl.AuthServiceImpl;
import com.tamamhuda.minimart.common.annotation.CurrentUser;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        TokenResponseDto response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        TokenResponseDto response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@CurrentUser User user) {
        UserDto response = authService.me(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(@Valid @RequestBody TokenRequest request) {
        RefreshResponseDto response = authService.refresh(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?>  logout(@Valid @RequestBody TokenRequest request) {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
