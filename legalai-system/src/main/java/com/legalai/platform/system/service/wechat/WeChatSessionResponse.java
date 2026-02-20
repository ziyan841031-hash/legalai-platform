package com.legalai.platform.system.service.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeChatSessionResponse {
    // 用户 openid
    private String openid;
    // 会话密钥
    @JsonProperty("session_key")
    private String sessionKey;
    // 用户 unionid
    private String unionid;
    // 错误码
    private Integer errcode;
    // 错误信息
    private String errmsg;
}
