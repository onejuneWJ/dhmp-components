package com.zznode.dhmp.jdbc.datasource;

import com.zznode.dhmp.jdbc.datasource.annotation.UseDynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/**
 * 动态数据源切面类
 *
 * @author 王俊
 * @date create in 2023/5/26 10:20
 * @see UseDynamicDataSource
 */
@Aspect
public class DynamicDataSourceAspect {

    private final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Pointcut("@annotation(com.zznode.dhmp.jdbc.datasource.annotation.UseDynamicDataSource) || " +
            " @within(com.zznode.dhmp.jdbc.datasource.annotation.UseDynamicDataSource)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        logger.info("starting point");
        String dataSourceType = getDataSource(point);
        logger.info("setting current dataSource with type: {}", dataSourceType);
        DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }

    }

    public String getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 方法上的注解优先级更高
        UseDynamicDataSource annotation = AnnotationUtils.findAnnotation(signature.getMethod(), UseDynamicDataSource.class);
        if (annotation != null) {
            return annotation.dataSourceType();
        }
        UseDynamicDataSource useDataSource = AnnotationUtils.findAnnotation(signature.getDeclaringType(), UseDynamicDataSource.class);
        Assert.state(useDataSource != null, "UseDynamicDataSource annotation must not be null");
        return useDataSource.dataSourceType();
    }
}
