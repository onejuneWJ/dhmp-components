package com.zznode.dhmp.data.process.sensitive.interceptor;

import com.zznode.dhmp.data.process.sensitive.SensitiveDataProcessor;
import com.zznode.dhmp.data.process.sensitive.annotation.Sensitive;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 * 数据脱敏advisor
 *
 * @author 王俊
 */
@SuppressWarnings("serial")
public class SensitiveAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;

    private final Pointcut pointcut;

    public SensitiveAdvisor(@Nullable Supplier<SensitiveDataProcessor> processor) {
        this.advice = buildAdvice(processor);
        this.pointcut = buildPointcut();
    }


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    private Advice buildAdvice(@Nullable Supplier<SensitiveDataProcessor> processor) {
        SensitiveInterceptor sensitiveInterceptor = new SensitiveInterceptor();
        sensitiveInterceptor.configure(processor);
        return sensitiveInterceptor;
    }

    /**
     * 构建一个切面点cut，用于匹配被{@link Sensitive @Sensitive}注解标记的类或方法。
     *
     * @return 返回一个组合了类和方法注解匹配的切面点。
     */
    protected Pointcut buildPointcut() {
        // 定义关注的注解类型为UseDynamicDataSource
        Class<? extends Annotation> annotationType = Sensitive.class;
        // 创建一个切面点cut，用于匹配被Sensitive注解标记的类
        Pointcut cpc = new AnnotationMatchingPointcut(annotationType, true);
        // 创建一个切面点cut，用于匹配被Sensitive注解标记的方法
        Pointcut mpc = new AnnotationMatchingPointcut(null, annotationType, true);
        // 将类和方法的切面点cut组合，并返回这个组合的切面点cut
        return new ComposablePointcut(cpc).union(mpc);
    }
}
