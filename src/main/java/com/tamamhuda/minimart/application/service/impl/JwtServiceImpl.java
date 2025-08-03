package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.service.JwtService;
import com.tamamhuda.minimart.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

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
