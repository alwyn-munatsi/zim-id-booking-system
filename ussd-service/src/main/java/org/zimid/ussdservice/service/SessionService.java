package org.zimid.ussdservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.zimid.ussdservice.model.UssdSession;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {

    private final RedisTemplate<String, UssdSession> redisTemplate;

    private static final String SESSION_PREFIX = "ussd:session:";
    private static final Duration SESSION_TIMEOUT = Duration.ofMinutes(5);

    public void saveSession(UssdSession session) {
        String key = SESSION_PREFIX + session.getSessionId();
        redisTemplate.opsForValue().set(key, session, SESSION_TIMEOUT);
        log.debug("Session saved: {}", session.getSessionId());
    }

    public UssdSession getSession(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        UssdSession session = redisTemplate.opsForValue().get(key);
        log.debug("Session retrieved: {}", sessionId);
        return session;
    }

    public void deleteSession(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        redisTemplate.delete(key);
        log.debug("Session deleted: {}", sessionId);
    }

    public boolean sessionExists(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}