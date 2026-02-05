package com.legalai.platform.admin.controller.wxapp.auth;

import com.legalai.platform.framework.security.token.TokenManager;
import com.legalai.platform.framework.security.token.TokenPair;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wxapp/auth")
public class WxAppAuthController {

    private final TokenManager tokenManager;

    // 构造方法注入 TokenManager
    public WxAppAuthController(TokenManager tokenManager) {
        // 保存依赖
        this.tokenManager = tokenManager;
    }

    // 微信小程序登录接口
    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@RequestBody WxAppLoginRequest request) {
        // TODO: 使用 code 调用微信接口并映射到用户主体
        TokenPair tokenPair = tokenManager.issueTokenPair("wxapp-user:" + request.code(), Map.of()); // 生成令牌对
        return ResponseEntity.ok(tokenPair); // 返回登录结果
    }

    // 刷新令牌接口
    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@RequestBody TokenRefreshRequest request) {
        TokenPair tokenPair = tokenManager.refreshToken(request.refreshToken()); // 刷新令牌
        return ResponseEntity.ok(tokenPair); // 返回刷新结果
    }
}
