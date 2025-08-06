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

    @Value("${spring.security.jwt.access.expiration-in-minutes}")
    private long accessTokenExpiration;

    @Value("${spring.security.jwt.refresh.expiration-in-days}")
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

    private Jwt decodeToken(String token, JwtDecoder decoder, String tokenType) {
        try {
            return decoder.decode(token);
        } catch (JwtException e) {
            try {
                JWTClaimsSet claims = SignedJWT.parse(token).getJWTClaimsSet();
                boolean isExpired = claims.getExpirationTime().before(new Date());

                if (isExpired) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, tokenType + " token is expired");

                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, tokenType + " token is invalid");
                }

            } catch (ParseException parseEx) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, tokenType + " token is malformed");
            }
        }
    }


    public String extractSubject(String accessToken) throws ResponseStatusException {
        return decodeToken(accessToken, accessTokenDecoder, "Access").getSubject();
    }

    public String extractUsernameFromRefresh(String refreshToken) throws ResponseStatusException {
            return decodeToken(refreshToken, refreshTokenDecoder, "Refresh").getSubject();

    }

    public Instant extractAccessTokenExpiresAt(String accessToken) throws ResponseStatusException {
        return decodeToken(accessToken, accessTokenDecoder, "Access").getExpiresAt();
    }

    public Instant extractRefreshTokenExpiresAt(String refreshToken) throws ResponseStatusException {
        return decodeToken(refreshToken, refreshTokenDecoder, "Refresh").getExpiresAt();
    }

}
