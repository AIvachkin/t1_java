package ru.t1.java.demo.aop;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.log.TimeLimitExceedLog;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TimeLimitExceedLoggingAspect {

    private final EntityManager entityManager;

    @Value("${track.time-limit-exceed}")
    private long timeLimitExceed;

    @Around("@annotation(ru.t1.java.demo.aop.TimeLimitedExecution)")
    public Object logTimeLimitExceed(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        if (executionTime > timeLimitExceed) {
            TimeLimitExceedLog timeLimitExceedLog = TimeLimitExceedLog.builder()
                    .methodSignature(joinPoint.getSignature().toShortString())
                    .executionTime(executionTime)
                    .build();
            entityManager.persist(timeLimitExceedLog);
        }

        return result;
    }
}