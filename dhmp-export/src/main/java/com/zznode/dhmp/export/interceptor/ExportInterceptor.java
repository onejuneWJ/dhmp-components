package com.zznode.dhmp.export.interceptor;

import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.ExportContextFactory;
import com.zznode.dhmp.export.ExportException;
import com.zznode.dhmp.export.ExportExecutionSupport;
import com.zznode.dhmp.export.constants.ReportType;
import com.zznode.dhmp.export.utils.ResponseHelper;
import com.zznode.dhmp.export.web.mvc.ServletHandlerMethodExportContextFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 导出方法拦截器
 * <p>
 * 支持拦截controller方法，也支持拦截service方法。
 * 也就是说，在controller层和service层标注了{@link com.zznode.dhmp.export.annotation.Export @Export}的方法都可以实现导出功能。
 * <p>
 * 不支持跟{@link Async} 一起使用，
 * 因为导出需要获取方法执行的返回值，而{@link Async}不支持有返回值的方法。
 *
 * @author 王俊
 */
public class ExportInterceptor extends ExportExecutionSupport implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        logger.debug("preparing ExportContext for export method: " + method);
        ExportContext exportContext = createExportContext(method);



        // 执行目标方法，获取返回的数据。发生异常直接抛出
        Object returnValue = invokeForExport(invocation);

        try {
            doExport(returnValue, exportContext);
        } catch (Throwable e) {
            ResponseHelper.setRequestResponseHeader(exportContext.response());
            if (e instanceof ExportException exportException) {
                throw new CommonException(exportException.getMessage());
            }
            ReportType type = exportContext.exportParam().getType();
            throw new CommonException("export.error", e, type.name());
        }
        return null;
    }


    /**
     * 调用方法获取返回的数据
     *
     * @param invocation 方法调用
     * @return 方法返回的数据
     */
    @Nullable
    protected Object invokeForExport(MethodInvocation invocation) throws Throwable {
        long getDataStart = System.nanoTime();
        Object result = invocation.proceed();
        long getDataEnd = System.nanoTime();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("fetch data cost %s ms", Duration.ofNanos(getDataEnd - getDataStart).toMillis()));
        }
        return result;
    }

    protected ExportContext createExportContext(Method method) {
        ExportContextFactory exportContextFactory = new ServletHandlerMethodExportContextFactory(method);
        return exportContextFactory.createContext();
    }

}
