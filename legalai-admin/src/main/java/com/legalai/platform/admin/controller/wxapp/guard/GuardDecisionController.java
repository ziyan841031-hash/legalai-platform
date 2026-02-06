package com.legalai.platform.admin.controller.wxapp.guard;

import com.legalai.platform.system.service.guard.GuardDecisionService;
import com.legalai.platform.system.service.guard.GuardDecisionService.GuardDecisionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guard")
public class GuardDecisionController {

    private final GuardDecisionService guardDecisionService;

    public GuardDecisionController(GuardDecisionService guardDecisionService) {
        this.guardDecisionService = guardDecisionService;
    }

    /**
     * 小程序护栏决策接口。
     * 前端点击“护栏决策”按钮后会调用该接口，返回是否放行以及原因/答案。
     */
    @PostMapping("/decision")
    public ResponseEntity<GuardDecisionResponse> decision(@RequestBody GuardDecisionRequest request) {
        // 调用护栏服务进行审核
        GuardDecisionResult result = guardDecisionService.evaluate(request.question());
        // 组装返回结构
        GuardDecisionResponse response = new GuardDecisionResponse(result.allowed(), result.reason(), result.answer());
        return ResponseEntity.ok(response);
    }
}
