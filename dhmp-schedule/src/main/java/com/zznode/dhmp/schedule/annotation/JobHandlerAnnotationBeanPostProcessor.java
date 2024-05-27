package com.zznode.dhmp.schedule.annotation;

import com.zznode.dhmp.schedule.JobHandlerRegistry;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean后置处理器，用于注册jobHandler
 *
 * @author 王俊
 */
public class JobHandlerAnnotationBeanPostProcessor implements DestructionAwareBeanPostProcessor, BeanPostProcessor, Ordered {


    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));


    // 将JobHandler.class定义为常量
    private static final Class<JobHandler> JOB_HANDLER_CLASS = JobHandler.class;


    private Optional<String> getJobCodeFromBean(Object bean) {

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);

        if (nonAnnotatedClasses.contains(targetClass)) {
            return Optional.empty();
        }

        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(targetClass, JOB_HANDLER_CLASS);
        if (attributes == null) {
            nonAnnotatedClasses.add(targetClass);
            return Optional.empty();
        }
        return Optional.of(attributes)
                .map(annotationAttributes -> annotationAttributes.getString("value"))
                .filter(StringUtils::hasText);

    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        getJobCodeFromBean(bean).ifPresent(jobCode -> JobHandlerRegistry.registerJobHandler(jobCode, beanName));
        return bean;
    }

    @Override
    public int getOrder() {
        // 优先级设置高一点，在创建代理之前处理，更快获取目标类
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        getJobCodeFromBean(bean).ifPresent(JobHandlerRegistry::removeJobHandler);
    }
}
