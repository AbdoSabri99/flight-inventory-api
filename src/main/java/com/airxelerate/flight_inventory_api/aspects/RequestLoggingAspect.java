package com.airxelerate.flight_inventory_api.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RequestLoggingAspect {

    // Log all controller methods
    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void logBeforeController(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        Object[] args = joinPoint.getArgs();
        // Mask sensitive data
        Object[] safeArgs = Arrays.stream(args)
                .map(arg -> {
                    if (arg != null && arg.toString().toLowerCase().contains("password")) {
                        return "****";
                    }
                    return arg;
                }).toArray();

        log.info("Entering {}.{} with arguments: {}", className, methodName, safeArgs);
    }

    @AfterReturning(pointcut = "within(@org.springframework.web.bind.annotation.RestController *)", returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Exiting {}.{} with result: {}", className, methodName, result);
    }
}

