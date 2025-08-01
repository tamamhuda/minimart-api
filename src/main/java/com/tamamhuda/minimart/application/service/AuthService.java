package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    public ResponseEntity<TokenResponseDto> login(LoginRequestDto request) throws UnauthorizedException;

    public ResponseEntity<TokenResponseDto> register(RegisterRequestDto request);

    public ResponseEntity<UserDto> me(User user) throws UnauthorizedException;

    public ResponseEntity<RefreshResponseDto> refresh(TokenRequest request);

    public ResponseEntity<?> logout(TokenRequest request);
}
