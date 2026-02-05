package com.legalai.platform.system.service.wechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatAuthService {

    private static final String GRANT_TYPE = "authorization_code";

    private final RestTemplate restTemplate;
    private final WeChatAuthProperties weChatAuthProperties;
    private final ObjectMapper objectMapper;

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
            String body = restTemplate.getForObject(url, String.class); // 调用微信接口
            log.info("[WECHAT] code2session response={}", body); // 输出返回日志
            return objectMapper.readValue(body, WeChatSessionResponse.class); // 解析返回结果
        } catch (Exception ex) {
            log.warn("[WECHAT] code2session failed: {}", ex.getMessage()); // 输出异常信息
            WeChatSessionResponse response = new WeChatSessionResponse();
            response.setErrcode(-1); // 设置错误码
            response.setErrmsg(ex.getMessage()); // 设置异常信息
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
