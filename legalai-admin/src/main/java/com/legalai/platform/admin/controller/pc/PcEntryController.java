package com.legalai.platform.admin.controller.pc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pc")
public class PcEntryController {

    // PC 端健康检查接口
    @GetMapping("/health")
    public String health() {
        // 返回健康状态
        return "pc-ok";
    }
}
