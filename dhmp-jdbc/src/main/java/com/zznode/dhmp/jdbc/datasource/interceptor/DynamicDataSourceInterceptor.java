package com.zznode.dhmp.jdbc.datasource.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

/**
 * 方法拦截器，拦截带有{@link com.zznode.dhmp.jdbc.datasource.annotation.UseDynamicDataSource @UseDynamicDataSource}注解的方法
 *
 * @author 王俊
 */
public class DynamicDataSourceInterceptor extends DynamicDataSourceAspectSupport implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);

        return invokeWithinDynamicDataSource(invocation.getMethod(), targetClass, invocation::proceed);
    }

}
