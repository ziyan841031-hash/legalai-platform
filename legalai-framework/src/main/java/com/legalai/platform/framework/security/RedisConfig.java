package com.legalai.platform.framework.security;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.database:0}")
    private int database;

    @Value("${spring.data.redis.timeout:3000}")
    private long timeout;

    // 创建 Redis 连接池配置
    @Bean
    public GenericObjectPoolConfig<?> poolConfig() {
        return new GenericObjectPoolConfig<>(); // 使用默认连接池配置
    }

    /**
     * 创建 Redis 连接工厂（带连接池）
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(GenericObjectPoolConfig<?> poolConfig) {
        log.info("初始化Redis连接: {}:{} (database: {})", host, port, database);

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setDatabase(database);

        if (password != null && !password.trim().isEmpty()) {
            config.setPassword(password);
            log.info("Redis密码: 已配置");
        } else {
            log.warn("Redis密码: 未配置");
        }

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
            .poolConfig(poolConfig)
            .commandTimeout(Duration.ofMillis(timeout))
            .shutdownTimeout(Duration.ofSeconds(2))
            .clientOptions(ClientOptions.builder()
                .autoReconnect(true)
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .socketOptions(SocketOptions.builder()
                    .keepAlive(true)
                    .connectTimeout(Duration.ofMillis(timeout))
                    .build())
                .build())
            .build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);

        try {
            factory.afterPropertiesSet();
            log.info("✅ Redis连接测试成功");
        } catch (Exception e) {
            log.error("❌ Redis连接失败", e);
            log.error("请检查:");
            log.error("1. Redis服务是否启动: redis-cli ping");
            log.error("2. 主机端口是否正确: {}:{}", host, port);
            log.error("3. 密码是否正确");
            log.error("4. 防火墙是否放行");
            throw new RuntimeException("Redis连接失败: " + e.getMessage(), e);
        }

        return factory;
    }

    // 创建 StringRedisTemplate
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory); // 注入连接工厂
    }
}
