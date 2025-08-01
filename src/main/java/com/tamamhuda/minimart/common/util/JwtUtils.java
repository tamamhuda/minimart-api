package com.tamamhuda.minimart.common.util;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final JwtEncoder accessTokenEncoder;
    private final JwtEncoder refreshTokenEncoder;
    private final JwtDecoder accessTokenDecoder;
    private final JwtDecoder refreshTokenDecoder;

    @Value("${JWT_ACCESS_EXPIRATION_IN_MINUTES}")
    private long accessTokenExpiration;

    @Value("${JWT_REFRESH_EXPIRATION_IN_DAYS}")
    private long refreshTokenExpiration;

    public JwtUtils(
            @Qualifier("accessTokenEncoder") JwtEncoder accessTokenEncoder,
            @Qualifier("refreshTokenEncoder") JwtEncoder refreshTokenEncoder,
            @Qualifier("accessTokenDecoder") JwtDecoder accessTokenDecoder,
            @Qualifier("refreshTokenDecoder") JwtDecoder refreshTokenDecoder
    ) {
        this.accessTokenEncoder = accessTokenEncoder;
        this.refreshTokenEncoder = refreshTokenEncoder;
        this.accessTokenDecoder = accessTokenDecoder;
        this.refreshTokenDecoder = refreshTokenDecoder;
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


    private Jwt decodeAccessToken(String accessToken) {
        try {
            return accessTokenDecoder.decode(accessToken);
        } catch (JwtException e) {
            try {
                JWTClaimsSet signedJWT = SignedJWT.parse(accessToken).getJWTClaimsSet();
                boolean isTokenExpired = signedJWT.getExpirationTime().before(new Date());

                if (!isTokenExpired) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is expired");
                }

                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid");

            } catch (ParseException er) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid");

            }
        }
    }

    private Jwt decodeRefreshToken(String refreshToken) {
        try {
            return refreshTokenDecoder.decode(refreshToken);
        } catch (JwtException e) {
            try {
                JWTClaimsSet signedJWT = SignedJWT.parse(refreshToken).getJWTClaimsSet();
                boolean isTokenExpired = signedJWT.getExpirationTime().before(new Date());

                if (!isTokenExpired) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is expired");
                }

                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid");

            } catch (ParseException er) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid");

            }
        }
    }


    public String extractSubject(String token) throws ResponseStatusException {
        return decodeAccessToken(token).getSubject();
    }

    public String extractUsernameFromRefresh(String refreshToken) throws ResponseStatusException {
        try {
            return decodeRefreshToken(refreshToken).getSubject();
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

    public Instant extractAccessTokenExpiresAt(String accessToken) throws ResponseStatusException {
        return decodeAccessToken(accessToken).getExpiresAt();
    }

    public Instant extractRefreshTokenExpiresAt(String refreshToken) throws ResponseStatusException {
        return decodeRefreshToken(refreshToken).getExpiresAt();
    }

}
