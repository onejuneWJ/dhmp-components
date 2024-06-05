package com.zznode.dhmp.data.process.sensitive.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 拦截方法，处理返回的数据
 *
 * @author 王俊
 */
public class SensitiveInterceptor extends SensitiveAspectSupport implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        if (proceed == null) {
            return null;
        }

        sensitiveData(proceed);


        return proceed;
    }
}
