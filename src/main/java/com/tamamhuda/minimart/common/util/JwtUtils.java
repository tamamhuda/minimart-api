package com.tamamhuda.minimart.common.util;

import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final JwtEncoder accessTokenEncoder;
    private final JwtEncoder refreshTokenEncoder;
    private final JwtDecoder accessTokenDecoder;
    private final JwtDecoder refreshTokenDecoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${JWT_ACCESS_EXPIRATION_IN_MINUTES}")
    private long accessTokenExpiration;

    @Value("${JWT_REFRESH_EXPIRATION_IN_DAYS}")
    private long refreshTokenExpiration;

    public JwtUtils(
            @Qualifier("accessTokenEncoder") JwtEncoder accessTokenEncoder,
            @Qualifier("refreshTokenEncoder") JwtEncoder refreshTokenEncoder,
            @Qualifier("accessTokenDecoder") JwtDecoder accessTokenDecoder,
            @Qualifier("refreshTokenDecoder") JwtDecoder refreshTokenDecoder,
            UserDetailsServiceImpl userDetailsService
    ) {
        this.accessTokenEncoder = accessTokenEncoder;
        this.refreshTokenEncoder = refreshTokenEncoder;
        this.accessTokenDecoder = accessTokenDecoder;
        this.refreshTokenDecoder = refreshTokenDecoder;
        this.userDetailsService = userDetailsService;
    }


    public String generateAccessToken(UserDetails userDetails) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(userDetails.getUsername())
                .expiresAt(now.plus(Duration.ofMinutes(accessTokenExpiration)))
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();

        return accessTokenEncoder
                .encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(userDetails.getUsername())
                .expiresAt(now.plus(Duration.ofDays(refreshTokenExpiration)))
                .build();

        return refreshTokenEncoder
                .encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
    }


    public String extractUsername(String token) throws ResponseStatusException {
        try {
            boolean isTokenExpired = isAccessTokenExpired(token);
            if (isTokenExpired) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access token is expired");
            }
            return accessTokenDecoder.decode(token).getSubject();
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid access token");
        }
    }

    public String extractUsernameFromRefresh(String refreshToken) throws ResponseStatusException {
        try {
            return refreshTokenDecoder.decode(refreshToken).getSubject();
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }


    public boolean isAccessTokenExpired(String token) {
        Instant expiresAt = accessTokenDecoder.decode(token).getExpiresAt();
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isAccessTokenExpired(token);
    }

    public boolean isRefreshTokenExpired(String token) {
        Instant expiresAt = refreshTokenDecoder.decode(token).getExpiresAt();
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        String username = refreshTokenDecoder.decode(token).getSubject();
        return username.equals(userDetails.getUsername()) && !isRefreshTokenExpired(token);
    }


    public String issueNewAccessToken(String refreshToken) throws ResponseStatusException {
        String username = extractUsernameFromRefresh(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!isRefreshTokenValid(refreshToken, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired or invalid");
        }

        return generateAccessToken(userDetails);
    }
}
