package com.legalai.platform.system.service.guard;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.legalai.platform.system.domain.GuardKeyword;
import com.legalai.platform.system.mapper.GuardKeywordMapper;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GuardDecisionService {

    private final GuardKeywordMapper guardKeywordMapper;
    private final DifyChatflowService difyChatflowService;

    public GuardDecisionService(GuardKeywordMapper guardKeywordMapper, DifyChatflowService difyChatflowService) {
        this.guardKeywordMapper = guardKeywordMapper;
        this.difyChatflowService = difyChatflowService;
    }

    /**
     * 进行护栏决策。
     *
     * @param question 用户问题
     * @return 护栏决策结果
     */
    public GuardDecisionResult evaluate(String question) {
        if (!StringUtils.hasText(question)) {
            return GuardDecisionResult.rejected("问题不能为空");
        }
        String normalized = question.toLowerCase(Locale.ROOT);
        List<GuardKeyword> keywords = guardKeywordMapper.selectList(
            new LambdaQueryWrapper<GuardKeyword>().eq(GuardKeyword::getEnabled, true)
        );
        // 命中高危关键字则拒绝
        for (GuardKeyword keyword : keywords) {
            if (keyword.getKeyword() == null) {
                continue;
            }
            if (normalized.contains(keyword.getKeyword().toLowerCase(Locale.ROOT))) {
                return GuardDecisionResult.rejected("命中高危关键字: " + keyword.getKeyword());
            }
        }
        // 通过护栏后调用 Dify Chatflow（占位）
        String answer = difyChatflowService.askChatflow(question);
        return GuardDecisionResult.allowed(answer);
    }

    /**
     * 护栏决策结果。
     *
     * @param allowed 是否放行
     * @param reason  拒绝原因
     * @param answer  通过后的回答
     */
    public record GuardDecisionResult(boolean allowed, String reason, String answer) {
        public static GuardDecisionResult allowed(String answer) {
            return new GuardDecisionResult(true, null, answer);
        }

        public static GuardDecisionResult rejected(String reason) {
            return new GuardDecisionResult(false, reason, null);
        }
    }
}
