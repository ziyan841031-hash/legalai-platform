package com.legalai.platform.admin.controller.wxapp.guard;

public record GuardDecisionResponse(boolean allowed, String reason, String answer) {
}
