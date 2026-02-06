package com.legalai.platform.admin.controller.wxapp.auth;

public record WxAppLoginRequest(String code, String phoneNumber, String phoneEncryptedData, String phoneIv) {
}
