package com.tamamhuda.minimart.application.service;


import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    public boolean isAccessTokenValid(String accessToken, UserDetails userDetails);

    public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails);

    public String issueNewAccessToken(String refreshToken);

    public String extractUsername(String token);

}

