package com.legalai.platform.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Map;

public interface TokenService {
    String generateToken(String subject, Map<String, Object> claims);

    Jws<Claims> parseToken(String token);
}
