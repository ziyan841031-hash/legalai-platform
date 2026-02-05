package com.legalai.platform.admin.controller.wxapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wxapp")
public class WxAppEntryController {

    @GetMapping("/health")
    public String health() {
        return "wxapp-ok";
    }
}
