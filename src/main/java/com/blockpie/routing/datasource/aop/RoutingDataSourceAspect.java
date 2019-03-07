package com.blockpie.routing.datasource.aop;

import com.blockpie.routing.datasource.RoutingDataSourceContextHolder;
import com.blockpie.routing.datasource.annotation.DataMaster;
import com.blockpie.routing.datasource.annotation.DataSlave;
import com.blockpie.routing.datasource.annotation.TargetDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class RoutingDataSourceAspect {
    @Pointcut("(@within(com.blockpie.routing.datasource.annotation.TargetDataSource) && @annotation(com.blockpie.routing.datasource.annotation.DataMaster)) " +
            "|| (@annotation(com.blockpie.routing.datasource.annotation.TargetDataSource) && @annotation(com.blockpie.routing.datasource.annotation.DataMaster))")
    public void masterPointcut() {

    }

    @Pointcut("(@within(com.blockpie.routing.datasource.annotation.TargetDataSource) && @annotation(com.blockpie.routing.datasource.annotation.DataSlave)) " +
            "|| (@annotation(com.blockpie.routing.datasource.annotation.TargetDataSource) && @annotation(com.blockpie.routing.datasource.annotation.DataSlave))")
    public void slavePointcut() {

    }

    @Around("masterPointcut() || slavePointcut()")
    public Object aroundMethod(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        Method method = methodSignature.getMethod();

        TargetDataSource targetDataSource = Optional.ofNullable(method.getAnnotation(TargetDataSource.class))
                .orElse(point.getTarget().getClass().getAnnotation(TargetDataSource.class));
        String sourceName = targetDataSource.value();

        if (method.getAnnotation(DataMaster.class) != null) {
            RoutingDataSourceContextHolder.changeToMaster(sourceName);
        } else if (method.getAnnotation(DataSlave.class) != null) {
            RoutingDataSourceContextHolder.changeToSlave(sourceName);
        }

        try {
            return point.proceed();
        } finally {
            RoutingDataSourceContextHolder.clear();
        }
    }
}
