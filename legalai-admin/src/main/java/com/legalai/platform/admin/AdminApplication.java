package com.legalai.platform.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.legalai.platform")
@MapperScan("com.legalai.platform.system.mapper")
public class AdminApplication {
    // 主启动方法
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(AdminApplication.class, args);
    }
}
