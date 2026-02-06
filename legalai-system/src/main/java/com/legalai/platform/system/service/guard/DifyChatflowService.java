package com.legalai.platform.system.service.guard;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.legalai.platform.system.domain.ToolConfig;
import com.legalai.platform.system.mapper.ToolConfigMapper;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

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
        DifyChatflowResponse response = callChatflow(question);
        String answer = response != null ? response.html() : null;
        String intent = response != null ? response.intent() : null;
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

    private DifyChatflowResponse callChatflow(String question) {
        if (!StringUtils.hasText(properties.getBaseUrl())) {
            return null;
        }
        String url = properties.getBaseUrl().replaceAll("/$", "");
        Map<String, Object> body = Map.of(
            "sys", Map.of(
                "query", question,
                "inputs", Map.of(
                    "api_guardrail_hit", 0,
                    "api_guardrail_message", ""
                )
            )
        );
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (StringUtils.hasText(properties.getApiKey())) {
                headers.setBearerAuth(properties.getApiKey());
            }
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            return restTemplate.postForObject(url, requestEntity, DifyChatflowResponse.class, Map.of());
        } catch (RestClientException ex) {
            return null;
        }
    }

    public record DifyChatflowResponse(
        @JsonAlias({"intent"})
        String intent,
        @JsonAlias({"risk_level"})
        String riskLevel,
        @JsonAlias({"html"})
        String html
    ) {
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
    }
}
