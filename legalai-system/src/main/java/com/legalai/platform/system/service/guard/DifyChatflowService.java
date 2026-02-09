package com.legalai.platform.system.service.guard;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.legalai.platform.system.domain.ToolConfig;
import com.legalai.platform.system.mapper.ToolConfigMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Slf4j
@Service
public class DifyChatflowService {

    private final DifyChatflowProperties properties;
    private final RestTemplate restTemplate;
    private final ToolConfigMapper toolConfigMapper;

    public DifyChatflowService(DifyChatflowProperties properties, RestTemplateBuilder restTemplateBuilder,
                               ToolConfigMapper toolConfigMapper) {
        this.properties = properties;
        this.restTemplate = restTemplateBuilder.build();
        this.toolConfigMapper = toolConfigMapper;
    }

    /**
     * 调用 Dify Chatflow 获取回答与意图，并根据意图推荐工具。
     *
     * @param question 用户问题
     * @return Chatflow 回答（若匹配到意图则附带推荐工具）
     */
    public String askChatflow(String question) {
        return askChatflow(question, 0, "");
    }

    public String askChatflow(String question, int apiGuardrailHit, String apiGuardrailMessage) {
        DifyChatflowResponse response = callChatflow(question, apiGuardrailHit, apiGuardrailMessage);
        String answer = response != null ? response.resolvedAnswer() : null;
        String intent = response != null ? response.resolvedIntent() : null;
        if (StringUtils.hasText(intent)) {
            ToolConfig tool = toolConfigMapper.selectOne(
                new LambdaQueryWrapper<ToolConfig>()
                    .eq(ToolConfig::getIntent, intent)
                    .eq(ToolConfig::getStatus, "ACTIVE")
            );
            if (tool != null) {
                String toolName = tool.getToolName() != null ? tool.getToolName() : tool.getToolCode();
                return String.format("%s\n推荐工具：%s", defaultAnswer(answer), toolName);
            }
        }
        return defaultAnswer(answer);
    }

    private String defaultAnswer(String answer) {
        return StringUtils.hasText(answer) ? answer : "已通过护栏，等待响应";
    }

    private DifyChatflowResponse callChatflow(String question, int apiGuardrailHit, String apiGuardrailMessage) {
        if (!StringUtils.hasText(properties.getBaseUrl())) {
            return null;
        }
        String normalizedApiKey = normalizeApiKey(properties.getApiKey());
        if (!StringUtils.hasText(normalizedApiKey)) {
            log.error("调用 Dify 失败：未配置有效 API Key，请检查 dify.api-key");
            return null;
        }
        String url = properties.getBaseUrl().replaceAll("/$", "") + "/chat-messages";
        Map<String, Object> body = Map.of(
            "inputs", Map.of(
                "api_guardrail_hit", String.valueOf(apiGuardrailHit),
                "api_guardrail_message", apiGuardrailMessage == null ? "" : apiGuardrailMessage
            ),
            "query", question,
            "response_mode", "blocking",
            "conversation_id", "",
            "user", properties.getUserId(),
            "files", List.of()
        );
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(normalizedApiKey);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            log.info("调用 Dify 开始, url={}, apiKeyPrefix={}, payload={}", url, maskApiKey(normalizedApiKey), body);
            DifyChatflowResponse response = restTemplate.postForObject(url, requestEntity, DifyChatflowResponse.class, Map.of());
            log.info("调用 Dify 成功, response={}", response);
            return response;
        } catch (HttpStatusCodeException ex) {
            log.error("调用 Dify 失败, status={}, responseBody={}, payload={}", ex.getStatusCode(), ex.getResponseBodyAsString(), body, ex);
            return null;
        } catch (RestClientException ex) {
            log.error("调用 Dify 异常, payload={}", body, ex);
            return null;
        }
    }

    private String normalizeApiKey(String apiKey) {
        if (!StringUtils.hasText(apiKey)) {
            return null;
        }
        String normalized = apiKey.trim();
        if (normalized.startsWith("Bearer ")) {
            normalized = normalized.substring("Bearer ".length()).trim();
        }
        if (!StringUtils.hasText(normalized) || "your-dify-api-key".equalsIgnoreCase(normalized)) {
            return null;
        }
        return normalized;
    }

    private String maskApiKey(String apiKey) {
        if (!StringUtils.hasText(apiKey)) {
            return "EMPTY";
        }
        if (apiKey.length() <= 6) {
            return "***";
        }
        return apiKey.substring(0, 3) + "***" + apiKey.substring(apiKey.length() - 3);
    }

    public record DifyChatflowResponse(
        @JsonAlias({"event"})
        String event,
        @JsonAlias({"task_id"})
        String taskId,
        @JsonAlias({"id"})
        String id,
        @JsonAlias({"message_id"})
        String messageId,
        @JsonAlias({"conversation_id"})
        String conversationId,
        @JsonAlias({"mode"})
        String mode,
        @JsonAlias({"answer"})
        String answer,
        @JsonAlias({"intent"})
        String intent,
        @JsonAlias({"risk_level"})
        String riskLevel,
        @JsonAlias({"html"})
        String html,
        @JsonAlias({"metadata"})
        Map<String, Object> metadata,
        @JsonAlias({"created_at"})
        Long createdAt
    ) {
        public String resolvedAnswer() {
            return StringUtils.hasText(answer) ? answer : html;
        }

        public String resolvedIntent() {
            if (StringUtils.hasText(intent)) {
                return intent;
            }
            if (metadata == null || metadata.isEmpty()) {
                return null;
            }
            Object directIntent = metadata.get("intent");
            if (directIntent instanceof String str && StringUtils.hasText(str)) {
                return str;
            }
            Object outputs = metadata.get("outputs");
            if (outputs instanceof Map<?, ?> outputsMap) {
                Object nestedIntent = outputsMap.get("intent");
                if (nestedIntent instanceof String str && StringUtils.hasText(str)) {
                    return str;
                }
            }
            return null;
        }
    }

    @Component
    @ConfigurationProperties(prefix = "dify")
    public static class DifyChatflowProperties {
        /**
         * Dify API 基础地址，如 https://api.dify.ai
         */
        private String baseUrl;

        /**
         * Dify API Key
         */
        private String apiKey;

        /**
         * Dify user 标识
         */
        private String userId = "abc-123";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
