package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;


public interface AuthService {
    TokenResponseDto login(LoginRequestDto request) throws UnauthorizedException;

    TokenResponseDto register(UserRequestDto request);

    RefreshResponseDto refresh(TokenRequest request);

    void logout(TokenRequest request);

    VerifyDto verify(String token);

    String resendVerification(User user);
}
