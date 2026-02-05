package com.legalai.platform.framework.security.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 构造方法注入过滤器
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        // 保存依赖
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // 配置安全过滤链
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // 配置 HttpSecurity
            .csrf(csrf -> csrf.disable()) // 关闭 CSRF
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/pc/health", "/api/wxapp/health")
                .permitAll() // 放行健康检查
                .requestMatchers("/api/wxapp/auth/login", "/api/pc/auth/login", "/api/auth/login")
                .permitAll() // 放行首次登录
                .requestMatchers("/api/wxapp/auth/**", "/api/auth/refresh")
                .permitAll() // 放行刷新接口
                .anyRequest().authenticated() // 其他接口需要认证
            )
            .httpBasic(Customizer.withDefaults()); // 保留默认 basic 配置
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 添加 JWT 过滤器
        return http.build(); // 构建过滤链
    }
}
