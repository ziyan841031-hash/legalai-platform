package com.legalai.platform.framework.security.web;

import com.legalai.platform.framework.security.token.TokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    // 构造方法注入 TokenManager
    public JwtAuthenticationFilter(TokenManager tokenManager) {
        // 保存依赖
        this.tokenManager = tokenManager;
    }

    // 过滤请求并解析 JWT
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // 获取授权头
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) { // 校验格式
            String token = authHeader.substring(7); // 截取 token
            try {
                String subject = tokenManager.validateAccessToken(token); // 校验访问令牌并获取主体
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList()); // 构造认证信息
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 设置详情
                SecurityContextHolder.getContext().setAuthentication(authentication); // 写入上下文
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext(); // 清理上下文
            }
        }
        filterChain.doFilter(request, response); // 放行请求
    }
}
