package com.legalai.platform.framework.security.token;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
public class RedisTokenStore implements TokenStore {

    private static final String KEY_PREFIX = "auth:refresh:";

    private final StringRedisTemplate redisTemplate;

    // 保存刷新令牌到 Redis
    @Override
    public void storeRefreshToken(String refreshTokenId, String subject, long expiresInSeconds) {
        String key = KEY_PREFIX + refreshTokenId; // 生成 Redis key
        try {
            redisTemplate.opsForValue().set(key, subject, Duration.ofSeconds(expiresInSeconds)); // 写入 Redis
        } catch (Exception ex) {
            log.warn("[REDIS] 保存刷新令牌失败: {}", ex.getMessage()); // 输出异常信息
        }
    }

    // 检查刷新令牌是否存在
    @Override
    public boolean existsRefreshToken(String refreshTokenId) {
        String key = KEY_PREFIX + refreshTokenId; // 生成 Redis key
        try {
            Boolean exists = redisTemplate.hasKey(key); // 查询是否存在
            return Boolean.TRUE.equals(exists); // 返回结果
        } catch (Exception ex) {
            log.warn("[REDIS] 查询刷新令牌失败: {}", ex.getMessage()); // 输出异常信息
            return false; // Redis 异常时返回 false
        }
    }

    // 删除刷新令牌
    @Override
    public void revokeRefreshToken(String refreshTokenId) {
        String key = KEY_PREFIX + refreshTokenId; // 生成 Redis key
        try {
            redisTemplate.delete(key); // 删除 key
        } catch (Exception ex) {
            log.warn("[REDIS] 删除刷新令牌失败: {}", ex.getMessage()); // 输出异常信息
        }
    }
}
