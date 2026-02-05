# 微信小程序授权登录接入说明

## 接入流程概览
1. 小程序端调用 `wx.login` 获取 `code`。
2. 前端将 `code` 传给后端登录接口 `/api/wxapp/auth/login`。
3. 后端通过微信 `code2session` 接口换取 `openid` / `session_key` / `unionid`。
4. 后端根据 `openid/unionid` 创建或关联用户。
5. 后端签发 access + refresh token 返回给前端。

## 小程序端示例
```js
wx.login({
  success: res => {
    const code = res.code;
    wx.request({
      url: 'https://api.example.com/api/wxapp/auth/login',
      method: 'POST',
      data: { code },
      success: (resp) => {
        const { accessToken, refreshToken } = resp.data;
        wx.setStorageSync('accessToken', accessToken);
        wx.setStorageSync('refreshToken', refreshToken);
      }
    })
  }
});
```

## 后端接口说明
### 1) 登录接口
- URL：`/api/wxapp/auth/login`
- 方法：`POST`
- 请求：`{ "code": "wx.login返回的code" }`
- 响应：`TokenPair`

### 2) 刷新接口
- URL：`/api/wxapp/auth/refresh`
- 方法：`POST`
- 请求：`{ "refreshToken": "refreshToken" }`
- 响应：`TokenPair`

## 微信 code2session 接口
- URL：`https://api.weixin.qq.com/sns/jscode2session`
- 请求参数：
  - `appid`：小程序 appid
  - `secret`：小程序 secret
  - `js_code`：前端传入的 code
  - `grant_type`：`authorization_code`

### 返回示例
```json
{
  "openid": "OPENID",
  "session_key": "SESSIONKEY",
  "unionid": "UNIONID",
  "errcode": 0,
  "errmsg": "ok"
}
```

## 安全建议
- 小程序端 access token 短期有效，refresh token 持久化。
- 所有业务接口必须校验 access token。
- 刷新接口仅接收 refresh token。
- 强制 HTTPS，避免 token 泄露。
- 通过 Redis 管理 refresh token，支持撤销和轮转。

