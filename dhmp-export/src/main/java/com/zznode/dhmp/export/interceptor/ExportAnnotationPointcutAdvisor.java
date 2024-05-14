package com.zznode.dhmp.export.interceptor;

import com.zznode.dhmp.export.annotation.Export;
import com.zznode.dhmp.export.support.exporter.ExporterFactory;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.util.function.Supplier;

/**
 * 导出的Advisor
 * <p>
 * 提供的切入点将会拦截{@link Export}标记的方法
 *
 * @author 王俊
 */
public class ExportAnnotationPointcutAdvisor extends AbstractPointcutAdvisor {
    @Serial
    private static final long serialVersionUID = -2035533642161505449L;

    private final Pointcut pointcut;
    private final Advice advice;

    public ExportAnnotationPointcutAdvisor(@Nullable Supplier<ExporterFactory> exporterFactory) {
        this.pointcut = new AnnotationMatchingPointcut(null, Export.class, false);
        this.advice = createExportAdvice(exporterFactory);
    }

    private Advice createExportAdvice(@Nullable Supplier<ExporterFactory> exporterFactory) {
        ExportInterceptor exportInterceptor = new ExportInterceptor();
        exportInterceptor.configure(exporterFactory);
        return exportInterceptor;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }
}
