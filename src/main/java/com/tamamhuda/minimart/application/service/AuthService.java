package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;


public interface AuthService {
    TokenResponseDto login(LoginRequestDto request) throws UnauthorizedException;

    TokenResponseDto register(RegisterRequestDto request);

    UserDto me(User user) throws UnauthorizedException;

    RefreshResponseDto refresh(TokenRequest request);

    void logout(TokenRequest request);

    VerifyDto verify(String token);
}
