package com.legalai.platform.system.service.guard;

import org.springframework.stereotype.Service;

@Service
public class DifyChatflowService {

    /**
     * 调用 Dify Chatflow 的占位方法。
     *
     * @param question 用户问题
     * @return Chatflow 回答
     */
    public String askChatflow(String question) {
        // TODO: 调用 Dify Chatflow API 获取回答
        return "已通过护栏，等待响应";
    }
}
