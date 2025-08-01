package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.SessionDto;
import com.tamamhuda.minimart.domain.entity.Session;

import java.util.UUID;

public interface SessionService  {

    public Session create(SessionDto session);

    public Session update(SessionDto session, String sessionId);

    public Session getById(UUID sessionId);

    public Session getByAccessToken(String accessToken);

    public Session getByRefreshToken(String refreshToken);

    public void refreshSession(String refreshToken, String accessToken);

    public void destroySessionByAccessToken(String accessToken);

    public void signSession(String accessToken, String refreshToken);

}
