package com.legalai.platform.framework.security.token;

public interface TokenStore {
    void storeRefreshToken(String refreshTokenId, String subject, long expiresInSeconds);

    boolean existsRefreshToken(String refreshTokenId);

    void revokeRefreshToken(String refreshTokenId);
}
