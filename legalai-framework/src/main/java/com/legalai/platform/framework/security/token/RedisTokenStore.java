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

    // 保存刷新令牌到 Redis
    @Override
    public void storeRefreshToken(String refreshTokenId, String subject, long expiresInSeconds) {
        String key = KEY_PREFIX + refreshTokenId; // 生成 Redis key
        redisTemplate.opsForValue().set(key, subject, Duration.ofSeconds(expiresInSeconds)); // 写入 Redis
    }

    // 检查刷新令牌是否存在
    @Override
    public boolean existsRefreshToken(String refreshTokenId) {
        String key = KEY_PREFIX + refreshTokenId; // 生成 Redis key
        Boolean exists = redisTemplate.hasKey(key); // 查询是否存在
        return Boolean.TRUE.equals(exists); // 返回结果
    }

    // 删除刷新令牌
    @Override
    public void revokeRefreshToken(String refreshTokenId) {
        String key = KEY_PREFIX + refreshTokenId; // 生成 Redis key
        redisTemplate.delete(key); // 删除 key
    }
}
