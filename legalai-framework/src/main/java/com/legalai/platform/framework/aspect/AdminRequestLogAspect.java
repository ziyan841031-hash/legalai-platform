package com.legalai.platform.framework.aspect;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AdminRequestLogAspect {

    // 记录 admin 控制器的请求与响应
    @Around("execution(* com.legalai.platform.admin.controller..*(..))")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString(); // 获取方法签名
        String args = Arrays.stream(joinPoint.getArgs())
            .map(arg -> arg == null ? "null" : arg.toString())
            .collect(Collectors.joining(", ")); // 拼接参数
        log.info("[ADMIN-REQ] method={}, args={}", method, args); // 输出请求日志
        Object result = joinPoint.proceed(); // 执行目标方法
        log.info("[ADMIN-RESP] method={}, result={}", method, result); // 输出响应日志
        return result; // 返回结果
    }
}
