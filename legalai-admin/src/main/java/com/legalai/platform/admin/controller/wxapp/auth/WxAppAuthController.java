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

    public WxAppAuthController(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@RequestBody WxAppLoginRequest request) {
        // TODO: exchange request.code with WeChat session API and map to user subject.
        TokenPair tokenPair = tokenManager.issueTokenPair("wxapp-user:" + request.code(), Map.of());
        return ResponseEntity.ok(tokenPair);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@RequestBody TokenRefreshRequest request) {
        TokenPair tokenPair = tokenManager.refreshToken(request.refreshToken());
        return ResponseEntity.ok(tokenPair);
    }
}
