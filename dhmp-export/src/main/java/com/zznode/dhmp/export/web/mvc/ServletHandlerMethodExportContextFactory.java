package com.zznode.dhmp.export.web.mvc;

import com.zznode.dhmp.export.annotation.Export;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * Servlet环境下对HandlerMethod的ExportContextFactory，默认的
 *
 * @author 王俊
 * @see org.springframework.web.method.HandlerMethod
 */
public class ServletHandlerMethodExportContextFactory extends AbstractServletExportContextFactory {

    /**
     * 拦截的方法
     */
    private final Method method;

    public ServletHandlerMethodExportContextFactory(Method method) {
        this.method = method;
    }

    /**
     * 从指定的方法中获取导出类的类型。
     * 该方法通过检查方法上是否标注了{@link Export}注解，并从中获取注解上的value值，
     * 代表需要导出的类的类型。
     *
     * @return 标注在方法上的{@link Export}注解中的value值，代表需要导出的类的类型。
     * @throws IllegalStateException 如果方法上没有标注{@link Export}注解，抛出此异常。
     */
    @Override
    protected Class<?> obtainExportClass() {
        Export export = method.getAnnotation(Export.class);
        // 代码能执行到这里，已经通过切面拦截，所以这里可以断定export不为null
        Assert.state(export != null, "Export annotation cannot be null");
        return export.value();
    }

}
