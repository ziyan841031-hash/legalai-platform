package com.legalai.platform.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Map;

public interface TokenService {
    // 生成访问令牌
    String generateToken(String subject, Map<String, Object> claims);

    // 解析并校验令牌
    Jws<Claims> parseToken(String token);
}
