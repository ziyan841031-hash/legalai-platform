package com.legalai.platform.framework.security.token;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTokenStore implements TokenStore {

    private static final String KEY_PREFIX = "auth:refresh:";

    private final StringRedisTemplate redisTemplate;

    @Override
    public void storeRefreshToken(String refreshTokenId, String subject, long expiresInSeconds) {
        String key = KEY_PREFIX + refreshTokenId;
        redisTemplate.opsForValue().set(key, subject, Duration.ofSeconds(expiresInSeconds));
    }

    @Override
    public boolean existsRefreshToken(String refreshTokenId) {
        String key = KEY_PREFIX + refreshTokenId;
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public void revokeRefreshToken(String refreshTokenId) {
        String key = KEY_PREFIX + refreshTokenId;
        redisTemplate.delete(key);
    }
}
