package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.mapper.RegisterRequestMapper;
import com.tamamhuda.minimart.application.mapper.UserDtoMapper;
import com.tamamhuda.minimart.application.service.AuthService;
import com.tamamhuda.minimart.application.service.JwtService;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.common.util.JwtUtils;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final UserServiceImpl userService;
    private final RegisterRequestMapper registerRequestMapper;
    private final UserDtoMapper userDtoMapper;
    private final SessionServiceImpl sessionService;
    private final JwtServiceImpl jwtService;

    public ResponseEntity<TokenResponseDto> login(LoginRequestDto request) throws UnauthorizedException {
        UserDetails user = userService.validateCredentials(request.getUsername(), request.getPassword());

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        sessionService.signSession(accessToken, refreshToken);

        TokenResponseDto loginResponseDto = TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok().body(loginResponseDto);
    }

    public ResponseEntity<TokenResponseDto> register(RegisterRequestDto request) {
        User rquestUser = registerRequestMapper.toEntity(request);

        User user = userService.createUser(rquestUser);

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        TokenResponseDto response = TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @Override
    public ResponseEntity<UserDto> me(User user) throws UnauthorizedException {
        return ResponseEntity.status(HttpStatus.OK).body(userDtoMapper.toDto(user));
    }

    @Override
    public ResponseEntity<RefreshResponseDto> refresh(TokenRequest request) throws UnauthorizedException {
        String refreshToken = request.getToken();
        String accessToken =  jwtService.issueNewAccessToken(refreshToken);
        sessionService.refreshSession(refreshToken, accessToken);

        RefreshResponseDto response = RefreshResponseDto.builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @Override
    public ResponseEntity<?> logout(TokenRequest request) {
        String accessToken = request.getToken();
        sessionService.destroySessionByAccessToken(accessToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


