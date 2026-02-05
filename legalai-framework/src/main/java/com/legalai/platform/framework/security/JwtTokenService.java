package com.legalai.platform.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

    private final JwtProperties jwtProperties;

    // 生成 JWT 访问令牌
    @Override
    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now(); // 获取当前时间
        Instant expiry = now.plusSeconds(jwtProperties.getExpirationSeconds()); // 计算过期时间
        return Jwts.builder() // 构建 JWT
            .setClaims(claims) // 设置自定义声明
            .setSubject(subject) // 设置主体
            .setIssuer(jwtProperties.getIssuer()) // 设置签发者
            .setIssuedAt(Date.from(now)) // 设置签发时间
            .setExpiration(Date.from(expiry)) // 设置过期时间
            .signWith(signingKey(), SignatureAlgorithm.HS256) // 使用密钥签名
            .compact(); // 生成字符串
    }

    // 解析并校验 JWT
    @Override
    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder() // 创建解析器
            .setSigningKey(signingKey()) // 设置签名密钥
            .build() // 构建解析器
            .parseClaimsJws(token); // 解析并校验
    }

    // 构建签名密钥
    private SecretKey signingKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8); // 获取密钥字节
        return Keys.hmacShaKeyFor(keyBytes); // 构建 HMAC 密钥
    }
}
