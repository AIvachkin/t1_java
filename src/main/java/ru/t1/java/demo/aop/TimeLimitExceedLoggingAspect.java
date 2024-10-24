package ru.t1.java.demo.aop;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.kafka.KafkaLogProducer;
import ru.t1.java.demo.model.log.TimeLimitExceedLog;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TimeLimitExceedLoggingAspect {

    private final EntityManager entityManager;
    private final KafkaLogProducer kafkaLogProducer;

    @Value("${track.time-limit.threshold}")
    private long timeLimitThreshold;

    @Value("${t1.kafka.topic.metric_trace}")
    private String metricTraceTopic;

    @Around("@annotation(ru.t1.java.demo.aop.TimeLimitedExecution)")
    public Object logTimeLimitExceed(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        if (executionTime > timeLimitThreshold) {
            TimeLimitExceedLog timeLimitExceedLog =
                    TimeLimitExceedLog.builder()
                            .executionTime(executionTime)
                            .methodName(joinPoint.getSignature().getName())
                            .methodParameters(Arrays.toString(joinPoint.getArgs()))
                            .build();

            entityManager.persist(timeLimitExceedLog);

            kafkaLogProducer.sendTo(metricTraceTopic, timeLimitExceedLog);
        }

        return result;
    }
}