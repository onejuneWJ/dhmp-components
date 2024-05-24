package com.zznode.dhmp.jdbc.datasource.interceptor;

import com.zznode.dhmp.jdbc.datasource.annotation.UseDynamicDataSource;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.io.Serial;
import java.lang.annotation.Annotation;

/**
 * 动态数据源的Advisor
 *
 * @author 王俊
 */
public class DynamicDataSourceAdvisor extends AbstractPointcutAdvisor {
    @Serial
    private static final long serialVersionUID = -5000589454760425074L;
    private final Advice advice;

    private final Pointcut pointcut;

    public DynamicDataSourceAdvisor() {
        this.advice = new DynamicDataSourceInterceptor();
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

    /**
     * 构建一个切面点cut，用于匹配被UseDynamicDataSource注解标记的类或方法。
     *
     * @return 返回一个组合了类和方法注解匹配的切面点cut。
     */
    protected Pointcut buildPointcut() {
        // 定义关注的注解类型为UseDynamicDataSource
        Class<? extends Annotation> annotationType = UseDynamicDataSource.class;
        // 创建一个切面点cut，用于匹配被UseDynamicDataSource注解标记的类
        Pointcut cpc = new AnnotationMatchingPointcut(annotationType, true);
        // 创建一个切面点cut，用于匹配被UseDynamicDataSource注解标记的方法
        Pointcut mpc = new AnnotationMatchingPointcut(null, annotationType, true);
        // 将类和方法的切面点cut组合，并返回这个组合的切面点cut
        return new ComposablePointcut(cpc).union(mpc);
    }

}
