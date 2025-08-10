package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.mapper.UserMapper;
import com.tamamhuda.minimart.application.mapper.UserRequestMapper;
import com.tamamhuda.minimart.application.service.AuthService;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.common.util.JwtUtils;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final UserServiceImpl userService;
    private final UserRequestMapper userRequestMapper;
    private final UserMapper userMapper;
    private final SessionServiceImpl sessionService;
    private final JwtServiceImpl jwtService;

    @Override
    public TokenResponseDto login(LoginRequestDto request) throws UnauthorizedException {
        UserDetails user = userService.validateCredentials(request.getUsername(), request.getPassword());

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        sessionService.signSession(accessToken, refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public TokenResponseDto register(RegisterRequestDto request) {
        User rquestUser = userRequestMapper.toEntity(request);

        User user = userService.createUser(rquestUser);

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    @Cacheable(cacheNames = "users", key = "#user.id")
    public UserDto me(User user) throws UnauthorizedException {
        return userMapper.toDto(user);
    }

    @Override
    public RefreshResponseDto refresh(TokenRequest request) throws UnauthorizedException {
        String refreshToken = request.getToken();
        String accessToken =  jwtService.issueNewAccessToken(refreshToken);
        sessionService.refreshSession(refreshToken, accessToken);

        return RefreshResponseDto.builder()
                .accessToken(accessToken)
                .build();

    }

    @Override
    public void logout(TokenRequest request) {
        String accessToken = request.getToken();
        sessionService.destroySessionByAccessToken(accessToken);
    }
}


