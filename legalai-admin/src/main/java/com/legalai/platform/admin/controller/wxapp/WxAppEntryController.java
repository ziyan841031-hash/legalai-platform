package com.legalai.platform.admin.controller.wxapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wxapp")
public class WxAppEntryController {

    // 微信小程序健康检查接口
    @GetMapping("/health")
    public String health() {
        // 返回健康状态
        return "wxapp-ok";
    }
}
