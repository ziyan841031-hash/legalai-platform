package com.legalai.platform.framework.security.token;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
public class InMemoryTokenStore implements TokenStore {

    private final Map<String, Instant> tokenExpirations = new ConcurrentHashMap<>();

    // 存储刷新令牌
    @Override
    public void storeRefreshToken(String refreshTokenId, String subject, long expiresInSeconds) {
        Instant expiresAt = Instant.now().plusSeconds(expiresInSeconds); // 计算过期时间
        tokenExpirations.put(refreshTokenId, expiresAt); // 保存令牌
        log.info("[MEMORY] 保存刷新令牌: {}", refreshTokenId); // 输出日志
    }

    // 判断刷新令牌是否存在
    @Override
    public boolean existsRefreshToken(String refreshTokenId) {
        Instant expiresAt = tokenExpirations.get(refreshTokenId); // 获取过期时间
        if (expiresAt == null) {
            return false; // 不存在
        }
        if (expiresAt.isBefore(Instant.now())) {
            tokenExpirations.remove(refreshTokenId); // 过期清理
            return false; // 已过期
        }
        return true; // 存在且未过期
    }

    // 撤销刷新令牌
    @Override
    public void revokeRefreshToken(String refreshTokenId) {
        tokenExpirations.remove(refreshTokenId); // 删除令牌
        log.info("[MEMORY] 撤销刷新令牌: {}", refreshTokenId); // 输出日志
    }
}
