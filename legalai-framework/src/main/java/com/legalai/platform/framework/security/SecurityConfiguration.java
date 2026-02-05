package com.legalai.platform.framework.security;

import com.legalai.platform.framework.security.token.TokenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, TokenProperties.class})
public class SecurityConfiguration {
}
