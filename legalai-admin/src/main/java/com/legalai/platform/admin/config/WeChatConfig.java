package com.legalai.platform.admin.config;

import com.legalai.platform.admin.service.wechat.WeChatAuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(WeChatAuthProperties.class)
public class WeChatConfig {

    // 提供 RestTemplate Bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // 创建 RestTemplate
    }
}
