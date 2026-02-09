package com.legalai.platform.framework.security.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    private long accessTokenSeconds = 1800;
    private long refreshTokenSeconds = 2592000;
    private String issuer = "legalai-platform";
    private String secret = "change-me";
}
