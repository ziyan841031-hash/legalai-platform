package com.legalai.platform.framework.security.token;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenPair {
    String accessToken;
    String refreshToken;
    long accessTokenExpiresIn;
    long refreshTokenExpiresIn;
}
