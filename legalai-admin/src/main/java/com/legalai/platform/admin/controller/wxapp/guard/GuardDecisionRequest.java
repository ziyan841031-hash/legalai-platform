package com.legalai.platform.admin.controller.wxapp.guard;

/**
 * 护栏决策请求体。
 *
 * @param question 用户问题内容
 */
public record GuardDecisionRequest(String question) {
}
