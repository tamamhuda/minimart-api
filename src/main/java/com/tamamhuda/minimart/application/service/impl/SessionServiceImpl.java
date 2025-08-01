package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.SessionDto;
import com.tamamhuda.minimart.application.mapper.SessionMapper;
import com.tamamhuda.minimart.application.service.SessionService;
import com.tamamhuda.minimart.common.util.JwtUtils;
import com.tamamhuda.minimart.domain.entity.Session;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final JwtUtils jwtUtils;
    private final UserServiceImpl userService;

    @Override
    public Session create(SessionDto session) {
        Session sessionEntity = sessionMapper.toEntity(session);
        return sessionRepository.save(sessionEntity);
    }

    @Override
    public Session update(SessionDto session, String sessionId) {
        Session sessionEntity = getById(UUID.fromString(sessionId));

        sessionMapper.updateSessionFromDto(session, sessionEntity);
        sessionEntity.setUpdatedAt(Instant.now());

        return sessionRepository.save(sessionEntity);
    }

    @Override
    public Session getById(UUID sessionId) {

        return  sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session with " + sessionId + " not found"));
    }

    @Override
    public Session getByAccessToken(String accessToken) {
        return sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session with " + accessToken + " not found"));
    }

    @Override
    public Session getByRefreshToken(String refreshToken) {
        System.out.println(refreshToken);
        return sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session with " + refreshToken + " not found"));
    }

    public void refreshSession(String refreshToken, String accessToken) {
        Session session = getByRefreshToken(refreshToken);
        session.setAccessToken(accessToken);
        session.setAccessTokenExpiresAt(jwtUtils.extractAccessTokenExpiresAt(accessToken));
        session.setUpdatedAt(Instant.now());
        sessionRepository.save(session);
    }

    @Override
    public void destroySessionByAccessToken(String accessToken) {
        Session session = getByAccessToken(accessToken);
        if (session.getIsSessionRevoked()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Session has been revoked");
        }
        session.setUpdatedAt(Instant.now());
        session.setAccessTokenExpiresAt(Instant.now());
        session.setIsSessionRevoked(true);
        session.setSessionRevokedAt(Instant.now());
        session.setRefreshTokenExpiresAt(Instant.now());
        sessionRepository.save(session);
    }

    @Override
    public void signSession(String accessToken, String refreshToken) {
        Instant accessTokenExpiresAt = jwtUtils.extractAccessTokenExpiresAt(accessToken);
        Instant refreshTokenExpiredAt = jwtUtils.extractRefreshTokenExpiresAt(refreshToken);
        User user = userService.getUserByUsername(jwtUtils.extractSubject(accessToken));

        SessionDto session = SessionDto.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isSessionRevoked(false)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshTokenExpiresAt(refreshTokenExpiredAt)
                .build();

        create(session);
    }
}
