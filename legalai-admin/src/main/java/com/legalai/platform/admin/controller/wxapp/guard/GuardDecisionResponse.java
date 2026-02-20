package com.legalai.platform.admin.controller.wxapp.guard;

/**
 * 护栏决策响应体。
 *
 * @param allowed 是否通过护栏
 * @param reason  拒绝原因（通过时为空）
 * @param answer  通过时的回答（拒绝时为空）
 */
public record GuardDecisionResponse(boolean allowed, String reason, String answer) {
}
