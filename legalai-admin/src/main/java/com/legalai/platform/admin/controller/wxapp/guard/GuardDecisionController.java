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

    @PostMapping("/decision")
    public ResponseEntity<GuardDecisionResponse> decision(@RequestBody GuardDecisionRequest request) {
        GuardDecisionResult result = guardDecisionService.evaluate(request.question());
        GuardDecisionResponse response = new GuardDecisionResponse(result.allowed(), result.reason(), result.answer());
        return ResponseEntity.ok(response);
    }
}
