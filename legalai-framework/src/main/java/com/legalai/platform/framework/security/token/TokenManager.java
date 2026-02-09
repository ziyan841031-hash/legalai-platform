package com.legalai.platform.framework.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private static final String CLAIM_TOKEN_TYPE = "token_type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    private final TokenProperties tokenProperties;
    private final TokenStore tokenStore;

    // 签发访问令牌与刷新令牌
    public TokenPair issueTokenPair(String subject, Map<String, Object> extraClaims) {
        String accessToken = createToken(subject, extraClaims, TOKEN_TYPE_ACCESS,
            tokenProperties.getAccessTokenSeconds()); // 生成访问令牌
        String refreshTokenId = UUID.randomUUID().toString(); // 生成刷新令牌 ID
        String refreshToken = createToken(subject, Map.of("jti", refreshTokenId), TOKEN_TYPE_REFRESH,
            tokenProperties.getRefreshTokenSeconds()); // 生成刷新令牌
        tokenStore.storeRefreshToken(refreshTokenId, subject, tokenProperties.getRefreshTokenSeconds()); // 存储刷新令牌
        return TokenPair.builder() // 构建令牌对
            .accessToken(accessToken) // 设置访问令牌
            .refreshToken(refreshToken) // 设置刷新令牌
            .accessTokenExpiresIn(tokenProperties.getAccessTokenSeconds()) // 设置访问令牌过期时间
            .refreshTokenExpiresIn(tokenProperties.getRefreshTokenSeconds()) // 设置刷新令牌过期时间
            .build(); // 返回结果
    }

    // 刷新令牌并轮转 refresh token
    public TokenPair refreshToken(String refreshToken) {
        Jws<Claims> parsed = parseToken(refreshToken); // 解析刷新令牌
        String tokenType = parsed.getBody().get(CLAIM_TOKEN_TYPE, String.class); // 读取类型
        if (!TOKEN_TYPE_REFRESH.equals(tokenType)) { // 校验类型
            throw new IllegalArgumentException("Invalid token type"); // 抛出异常
        }
        String refreshTokenId = parsed.getBody().get("jti", String.class); // 读取 jti
        if (!tokenStore.existsRefreshToken(refreshTokenId)) { // 校验是否存在
            throw new IllegalStateException("Refresh token revoked"); // 抛出异常
        }
        String subject = parsed.getBody().getSubject(); // 获取主体
        tokenStore.revokeRefreshToken(refreshTokenId); // 轮转时撤销旧 refresh
        return issueTokenPair(subject, Map.of()); // 签发新令牌对
    }

    // 解析并校验令牌
    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder() // 创建解析器
            .setSigningKey(signingKey()) // 设置签名密钥
            .build() // 构建解析器
            .parseClaimsJws(token); // 解析令牌
    }

    // 校验访问令牌并返回主体
    public String validateAccessToken(String token) {
        Jws<Claims> parsed = parseToken(token); // 解析令牌
        String tokenType = parsed.getBody().get(CLAIM_TOKEN_TYPE, String.class); // 获取令牌类型
        if (!TOKEN_TYPE_ACCESS.equals(tokenType)) { // 校验类型
            throw new IllegalArgumentException("Invalid access token type"); // 抛出异常
        }
        return parsed.getBody().getSubject(); // 返回主体
    }

    // 生成 JWT 字符串
    private String createToken(String subject, Map<String, Object> claims, String tokenType, long expiresInSeconds) {
        Instant now = Instant.now(); // 获取当前时间
        Instant expiry = now.plusSeconds(expiresInSeconds); // 计算过期时间
        return Jwts.builder() // 构建 JWT
            .setClaims(claims) // 设置自定义声明
            .setSubject(subject) // 设置主体
            .setIssuer(tokenProperties.getIssuer()) // 设置签发者
            .claim(CLAIM_TOKEN_TYPE, tokenType) // 设置令牌类型
            .setIssuedAt(Date.from(now)) // 设置签发时间
            .setExpiration(Date.from(expiry)) // 设置过期时间
            .signWith(signingKey(), SignatureAlgorithm.HS256) // 使用密钥签名
            .compact(); // 返回字符串
    }

    // 构建签名密钥
    private SecretKey signingKey() {
        byte[] keyBytes = tokenProperties.getSecret().getBytes(StandardCharsets.UTF_8); // 获取密钥字节
        return Keys.hmacShaKeyFor(keyBytes); // 构建 HMAC 密钥
    }
}
