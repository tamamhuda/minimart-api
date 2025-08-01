package com.tamamhuda.minimart.application.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.tamamhuda.minimart.application.service.impl.SessionServiceImpl;
import com.tamamhuda.minimart.common.util.JwtUtils;
import com.tamamhuda.minimart.domain.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.Date;


@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtUtils jwtUtils;
    private final SessionServiceImpl sessionService;
    private final UserDetailsService userDetailsService;

    public boolean isAccessTokenValid(String accessToken, UserDetails userDetails) {
        boolean isRevoked = sessionService.getByAccessToken(accessToken).getIsSessionRevoked();
        if (isRevoked) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access token is revoked");
        }

        return extractUsername(accessToken).equals(userDetails.getUsername());
    }

    public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails) {
        boolean isRevoked = sessionService.getByRefreshToken(refreshToken).getIsSessionRevoked();
        if (isRevoked) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is revoked");
        }

        return jwtUtils.extractUsernameFromRefresh(refreshToken).equals(userDetails.getUsername());
    }

    public String issueNewAccessToken(String refreshToken) {
        String username = jwtUtils.extractUsernameFromRefresh(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!isRefreshTokenValid(refreshToken, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired or invalid");
        }

        return jwtUtils.generateAccessToken(userDetails);
    }


    public String extractUsername(String token) {
        return jwtUtils.extractSubject(token);
    }

}

