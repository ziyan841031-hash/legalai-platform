package com.legalai.platform.framework.security.token;

public interface TokenStore {
    // 存储刷新令牌
    void storeRefreshToken(String refreshTokenId, String subject, long expiresInSeconds);

    // 判断刷新令牌是否存在
    boolean existsRefreshToken(String refreshTokenId);

    // 撤销刷新令牌
    void revokeRefreshToken(String refreshTokenId);
}
