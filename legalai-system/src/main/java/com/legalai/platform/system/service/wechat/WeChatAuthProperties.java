package com.legalai.platform.system.service.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wechat.miniapp")
public class WeChatAuthProperties {
    // 小程序 AppId
    private String appId;
    // 小程序密钥
    private String secret;
    // code2session 接口地址
    private String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
}
