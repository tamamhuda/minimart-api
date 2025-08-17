package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.*;
import com.tamamhuda.minimart.application.mapper.UserMapper;
import com.tamamhuda.minimart.application.mapper.UserRequestMapper;
import com.tamamhuda.minimart.application.service.AuthService;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.common.util.JwtUtils;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.enums.OtpStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final MailServiceImpl mailServiceImpl;
    private final VerificationServiceImpl verificationServiceImpl;

    @Override
    public TokenDto login(LoginRequestDto request) throws UnauthorizedException {
        UserDetails user = userService.validateCredentials(request.getUsername(), request.getPassword());

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        sessionService.signSession(accessToken, refreshToken);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    @Transactional
    public TokenDto register(UserRequestDto request) {
        User rquestUser = userRequestMapper.toEntity(request);

        User user = userService.createUser(rquestUser);

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        String otpToken = verificationServiceImpl.generateOtpForUser(user.getUsername());
        mailServiceImpl.sendEmailVerification(user, otpToken);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public RefreshTokenDto refresh(TokenRequest request) throws UnauthorizedException {
        String refreshToken = request.getToken();
        String accessToken =  jwtService.issueNewAccessToken(refreshToken);
        sessionService.refreshSession(refreshToken, accessToken);

        return RefreshTokenDto.builder()
                .accessToken(accessToken)
                .build();

    }

    @Override
    public void logout(TokenRequest request) {
        String accessToken = request.getToken();
        sessionService.destroySessionByAccessToken(accessToken);
    }

    @Override
    public VerifyDto verify(String token) {
        OtpStatus status = verificationServiceImpl.verifyOtpForUser(token);
        String message = switch (status) {
            case SUCCESS -> "Your account has been successfully verified! You can now log in.";
            case INVALID -> "Invalid verification link.";
            case FAILED -> "Verification link is incorrect. Please try again.";
            case EXPIRED -> "Verification link has expired. Please request a new verification email.";
            default -> "Verification link error";
        };

        return VerifyDto.builder()
                .status(status)
                .message(message)
                .build();
    }

    @Override
    public String resendVerification(User user) {
        String otpToken = verificationServiceImpl.generateOtpForUser(user.getUsername());
        mailServiceImpl.sendEmailVerification(user, otpToken);
        return "OK";
    }
}


