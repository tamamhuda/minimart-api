package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.LoginRequestDto;
import com.tamamhuda.minimart.application.dto.RegisterRequestDto;
import com.tamamhuda.minimart.application.dto.TokenResponseDto;
import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.application.mapper.RegisterRequestMapper;
import com.tamamhuda.minimart.common.util.JwtUtils;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface AuthService {
    public ResponseEntity<TokenResponseDto> login(LoginRequestDto request) throws UnauthorizedException;

    public ResponseEntity<TokenResponseDto> register(RegisterRequestDto request);

    public ResponseEntity<UserDto> me() throws UnauthorizedException;
}
