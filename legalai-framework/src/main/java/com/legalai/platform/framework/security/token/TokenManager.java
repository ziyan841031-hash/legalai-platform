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

    public TokenPair issueTokenPair(String subject, Map<String, Object> extraClaims) {
        String accessToken = createToken(subject, extraClaims, TOKEN_TYPE_ACCESS, tokenProperties.getAccessTokenSeconds());
        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = createToken(subject, Map.of("jti", refreshTokenId), TOKEN_TYPE_REFRESH,
            tokenProperties.getRefreshTokenSeconds());
        tokenStore.storeRefreshToken(refreshTokenId, subject, tokenProperties.getRefreshTokenSeconds());
        return TokenPair.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiresIn(tokenProperties.getAccessTokenSeconds())
            .refreshTokenExpiresIn(tokenProperties.getRefreshTokenSeconds())
            .build();
    }

    public TokenPair refreshToken(String refreshToken) {
        Jws<Claims> parsed = parseToken(refreshToken);
        String tokenType = parsed.getBody().get(CLAIM_TOKEN_TYPE, String.class);
        if (!TOKEN_TYPE_REFRESH.equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type");
        }
        String refreshTokenId = parsed.getBody().get("jti", String.class);
        if (!tokenStore.existsRefreshToken(refreshTokenId)) {
            throw new IllegalStateException("Refresh token revoked");
        }
        String subject = parsed.getBody().getSubject();
        tokenStore.revokeRefreshToken(refreshTokenId);
        return issueTokenPair(subject, Map.of());
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey())
            .build()
            .parseClaimsJws(token);
    }

    private String createToken(String subject, Map<String, Object> claims, String tokenType, long expiresInSeconds) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expiresInSeconds);
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuer(tokenProperties.getIssuer())
            .claim(CLAIM_TOKEN_TYPE, tokenType)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .signWith(signingKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private SecretKey signingKey() {
        byte[] keyBytes = tokenProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
