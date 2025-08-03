package com.tamamhuda.minimart.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

    @Value("${spring.security.jwt.access.secret-key}")
    private String accessSecret;

    @Value("${spring.security.jwt.refresh.secret-key}")
    private String refreshSecret;

    @Bean
    @Qualifier("accessTokenEncoder")
    public JwtEncoder accessTokenEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(accessSecret.getBytes()));
    }


    @Bean
    @Qualifier("refreshTokenEncoder")
    public JwtEncoder refreshTokenEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(refreshSecret.getBytes()));
    }

    @Bean
    @Qualifier("accessTokenDecoder")
    public JwtDecoder accessTokenDecoder() {
        byte[] bytes = accessSecret.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length,"HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(originalKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    @Qualifier("refreshTokenDecoder")
    public JwtDecoder refreshTokenDecoder() {
        byte[] bytes = refreshSecret.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length,"HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(originalKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
