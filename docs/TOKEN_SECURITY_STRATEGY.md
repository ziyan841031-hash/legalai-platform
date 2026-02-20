# Token 安全策略设计（JWT + Redis）

## 目标
- 所有接口访问必须携带 `Authorization: Bearer <accessToken>`。
- Access Token 只用于访问资源，短时有效。
- Refresh Token 只用于刷新 Access Token，长时有效且可撤销。
- Redis 用于 refresh token 生命周期管理与撤销。

## 令牌类型
- **Access Token**：短期有效（默认 30 分钟），仅用于访问业务接口。
- **Refresh Token**：长期有效（默认 30 天），仅用于换取新的 Access Token。

## 主要字段
- `sub`：用户唯一标识（userId / openId / unionId）。
- `iss`：签发方（平台标识）。
- `iat` / `exp`：签发时间与过期时间。
- `token_type`：`access` 或 `refresh`。
- `jti`：refresh token 的唯一 ID，用于 Redis 存储与撤销。

## Redis 设计
- Key：`auth:refresh:<jti>`
- Value：`subject`
- TTL：refresh token 过期时间

### 处理逻辑
1. 登录成功生成 access + refresh。
2. 将 refresh 的 `jti` 存入 Redis（用于撤销与失效）。
3. 刷新时校验 refresh token：
   - JWT 签名、有效期、`token_type=refresh`。
   - Redis 中存在 `auth:refresh:<jti>`。
4. 刷新成功后：
   - **撤销旧 refresh**（删除旧 `jti`）。
   - 签发新 access + refresh。

## 安全策略建议
- **短 Access Token + 长 Refresh Token**：减少 access token 泄露风险。
- **Refresh 轮转**：刷新成功立即撤销旧 refresh。
- **Redis 黑名单**：对疑似泄露或强制登出场景可主动删除 refresh。
- **设备隔离**：如需多设备登录，可将 refresh 绑定设备标识，key 设计为 `auth:refresh:<deviceId>:<jti>`。
- **签名密钥强度**：HS256 需要足够长度（建议 >= 32 字节）。
- **HTTPS 必须开启**：防止 token 被中间人窃取。
- **最小权限原则**：access token 中只放必要的授权声明。

## 接口要求
- 业务接口：必须验证 access token。
- 刷新接口：只允许 refresh token。
- 登录接口：不需要 token。

## 实现位置
- 令牌签发与刷新：`TokenManager`。
- Refresh 存储：`RedisTokenStore`。
- 认证过滤器：`JwtAuthenticationFilter`。
- 安全配置：`SecurityConfig`。

