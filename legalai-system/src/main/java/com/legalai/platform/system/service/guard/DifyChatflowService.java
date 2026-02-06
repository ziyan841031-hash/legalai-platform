package com.legalai.platform.system.service.guard;

import org.springframework.stereotype.Service;

@Service
public class DifyChatflowService {

    public String askChatflow(String question) {
        // TODO: 调用 Dify Chatflow API 获取回答
        return "已通过护栏，等待响应";
    }
}
