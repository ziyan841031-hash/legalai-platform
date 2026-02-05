package com.legalai.platform.admin.controller.wxapp.auth;

import com.legalai.platform.framework.security.token.TokenManager;
import com.legalai.platform.framework.security.token.TokenPair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.legalai.platform.system.service.wechat.WeChatAuthService;
import com.legalai.platform.system.service.wechat.WeChatSessionResponse;

@RestController
@RequestMapping("/api/wxapp/auth")
public class WxAppAuthController {

    private final TokenManager tokenManager;
    private final WeChatAuthService weChatAuthService;

    // 构造方法注入 TokenManager
    public WxAppAuthController(TokenManager tokenManager, WeChatAuthService weChatAuthService) {
        // 保存依赖
        this.tokenManager = tokenManager;
        this.weChatAuthService = weChatAuthService;
    }

    // 微信小程序登录接口
    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@RequestBody WxAppLoginRequest request) {
        // 使用 code 调用微信接口
        WeChatSessionResponse sessionResponse = weChatAuthService.exchangeCode(request.code()); // 获取微信会话
        String subject = weChatAuthService.buildSubject(sessionResponse); // 生成主体
        Map<String, Object> claims = new java.util.HashMap<>(weChatAuthService.buildClaims(sessionResponse)); // 复制声明
        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            claims.put("phoneNumber", request.phoneNumber()); // 追加手机号声明
        }
        TokenPair tokenPair = tokenManager.issueTokenPair(subject, claims); // 生成令牌对
        return ResponseEntity.ok(tokenPair); // 返回登录结果
    }

    // 刷新令牌接口
    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@RequestBody TokenRefreshRequest request) {
        TokenPair tokenPair = tokenManager.refreshToken(request.refreshToken()); // 刷新令牌
        return ResponseEntity.ok(tokenPair); // 返回刷新结果
    }
}
