package com.legalai.platform.admin.controller.miniapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/miniapp")
public class MiniAppEntryController {

    @GetMapping("/health")
    public String health() {
        return "miniapp-ok";
    }
}
