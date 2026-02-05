package com.legalai.platform.admin.controller.pc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pc")
public class PcEntryController {

    @GetMapping("/health")
    public String health() {
        return "pc-ok";
    }
}
