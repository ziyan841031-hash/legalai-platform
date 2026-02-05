package com.legalai.platform.admin.service.wechat;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class WeChatAuthService {

    private static final String GRANT_TYPE = "authorization_code";

    private final RestTemplate restTemplate;
    private final WeChatAuthProperties weChatAuthProperties;

    // 调用微信 code2session 接口
    public WeChatSessionResponse exchangeCode(String code) {
        String url = UriComponentsBuilder
            .fromHttpUrl(weChatAuthProperties.getCode2SessionUrl())
            .queryParam("appid", weChatAuthProperties.getAppId())
            .queryParam("secret", weChatAuthProperties.getSecret())
            .queryParam("js_code", code)
            .queryParam("grant_type", GRANT_TYPE)
            .toUriString();
        try {
            return restTemplate.getForObject(url, WeChatSessionResponse.class); // 调用微信接口
        } catch (RestClientException ex) {
            WeChatSessionResponse response = new WeChatSessionResponse();
            response.setErrcode(-1); // 设置错误码
            response.setErrmsg("call wechat api failed"); // 设置错误信息
            return response; // 返回失败结果
        }
    }

    // 组装登录主体信息
    public String buildSubject(WeChatSessionResponse response) {
        if (response.getUnionid() != null && !response.getUnionid().isBlank()) {
            return "wxapp-unionid:" + response.getUnionid(); // 优先使用 unionid
        }
        return "wxapp-openid:" + response.getOpenid(); // 回退使用 openid
    }

    // 生成额外声明信息
    public Map<String, Object> buildClaims(WeChatSessionResponse response) {
        return Map.of( // 构建声明内容
            "openid", response.getOpenid(), // 传递 openid
            "unionid", response.getUnionid() == null ? "" : response.getUnionid() // 传递 unionid
        );
    }
}
