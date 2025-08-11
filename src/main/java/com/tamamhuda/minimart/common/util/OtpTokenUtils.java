package com.tamamhuda.minimart.common.util;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tamamhuda.minimart.application.dto.OtpTokenDto;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.enums.OtpStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class OtpTokenUtils {

    private final JwtEncoder otpTokenEncoder;
    private final JwtDecoder otpTokenDecoder;

    @Value("${spring.totp.expiration-in-minutes}")
    private long otpTokenExpiration;

    public OtpTokenUtils(
            @Qualifier("otpTokenEncoder") JwtEncoder otpTokenEncoder,
            @Qualifier("otpTokenDecoder") JwtDecoder otpTokenDecoder
    ) {
        this.otpTokenEncoder = otpTokenEncoder;
        this.otpTokenDecoder = otpTokenDecoder;
    }

    public String generateOtpToken(String username, String otp) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofMinutes(otpTokenExpiration)))
                .subject(username)
                .claim("otp", otp)
                .build();

        return otpTokenEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        )).getTokenValue();
    }

    public Jwt decodeOtpToken(String token) {
        try {
            return otpTokenDecoder.decode(token);
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid OTP token");
        }
    }


    public OtpTokenDto extractOtpToken(String token) {
        Jwt jwt = decodeOtpToken(token);
        return OtpTokenDto.builder()
                .otp(jwt.getClaim("otp"))
                .username(jwt.getSubject())
                .build();
    }

    public OtpStatus extractValidateOtpToken(String token) {
        try {
            JWTClaimsSet claims = SignedJWT.parse(token).getJWTClaimsSet();
            boolean isTokenExpired =  claims.getExpirationTime().before(new Date());;
            return isTokenExpired ? OtpStatus.EXPIRED : OtpStatus.INVALID;
        } catch (Exception e) {
            return OtpStatus.INVALID;
        }
    }

}
