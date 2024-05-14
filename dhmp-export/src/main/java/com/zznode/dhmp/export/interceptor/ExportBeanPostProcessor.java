package com.zznode.dhmp.export.interceptor;

import com.zznode.dhmp.export.annotation.Export;
import com.zznode.dhmp.export.support.exporter.ExporterFactory;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.util.function.Supplier;

/**
 * Bean后处理器，通过向公开的代理(现有的AOP代理或新生成的实现所有目标接口的代理)添加相应的{@link ExportAnnotationPointcutAdvisor}，
 * 自动将导出行为应用于在方法级别携带{@link Export}注释的任何Bean
 *
 * @author 王俊
 */
public class ExportBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    @Serial
    private static final long serialVersionUID = -6228766854641003643L;

    @Nullable
    private Supplier<ExporterFactory> exporterFactory;

    public void configure(@Nullable Supplier<ExporterFactory> exporterFactory) {
        this.exporterFactory = exporterFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.advisor = new ExportAnnotationPointcutAdvisor(exporterFactory);
    }

}
