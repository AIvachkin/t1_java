package ru.t1.java.demo.aop;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.log.DataSourceErrorLog;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionLoggingAspect {

    private final EntityManager entityManager;

    @AfterThrowing(pointcut = "execution(* ru.t1.java.demo.*(..))"
            , throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        DataSourceErrorLog errorLog = DataSourceErrorLog.builder()
                .stackTrace(ExceptionUtils.getStackTrace(exception))
                .message(exception.getMessage())
                .methodSignature(joinPoint.getSignature().toShortString())
                .build();
        entityManager.persist(errorLog);
    }
}