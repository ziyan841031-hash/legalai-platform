package com.legalai.platform.framework.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    // 创建 Redis 连接工厂
    @Bean
    @ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(); // 使用 Jedis 连接
    }

    // 创建 StringRedisTemplate
    @Bean
    @ConditionalOnProperty(prefix = "spring.data.redis", name = "host")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory); // 注入连接工厂
    }
}
