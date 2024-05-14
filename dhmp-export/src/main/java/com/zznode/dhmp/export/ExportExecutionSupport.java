package com.zznode.dhmp.export;

import com.zznode.dhmp.export.support.exporter.DefaultExporterFactory;
import com.zznode.dhmp.export.support.exporter.Exporter;
import com.zznode.dhmp.export.support.exporter.ExporterFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.http.HttpEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.function.SingletonSupplier;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * 导出切面超类，提供导出切面执行逻辑相关方法
 *
 * @author 王俊
 */
public abstract class ExportExecutionSupport extends ApplicationObjectSupport {

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * ExporterFactory可能从容器中获取用户自定义的ExporterFactory
     */
    private SingletonSupplier<ExporterFactory> exporterFactory = SingletonSupplier.of(DefaultExporterFactory::new);

    public void configure(@Nullable Supplier<ExporterFactory> exporterFactory) {
        this.exporterFactory = new SingletonSupplier<>(exporterFactory, DefaultExporterFactory::new);
    }

    /**
     * 执行导出操作的函数。
     *
     * @param data          要导出的数据。
     * @param exportContext 导出上下文，包含导出操作所需的全部上下文信息。
     * @throws Exception 如果导出过程中遇到任何错误，则抛出异常。
     */
    protected void doExport(@Nullable Object data, ExportContext exportContext) throws Exception {

        Iterable<?> iterable = obtainExportData(data);

        // 如果返回的数据是空的,则不执行导出
        if (iterable == null) {
            return;
        }
        ExporterFactory factory = exporterFactory.obtain();
        // 创建导出器实例，基于传入的导出上下文。
        Exporter exporter = factory.createExporter(exportContext);
        long exportStart = System.nanoTime();
        // 执行导出操作，传入要导出的数据。
        exporter.export(iterable);
        long exportEnd = System.nanoTime();
        // 如果日志级别为DEBUG，则记录导出操作的执行时间。
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("export data cost %s ms", Duration.ofNanos(exportEnd - exportStart).toMillis()));
        }
    }

    /**
     * 从方法返回的数据中获取真实的导出数据
     * <p>
     * 本方法旨在处理并返回特定格式的导出数据。它接受一个方法调用的结果作为输入，根据结果的类型来决定如何处理和返回数据。
     * 如果结果是{@code null}，则直接返回{@code null}。
     *
     * @param result 方法调用返回的结果，可以是任何类型。
     * @return 导出的数据迭代器。如果结果为{@code null}或不支持的类型，则返回{@code null}。
     * @throws IllegalArgumentException 如果结果类型既不是{@code Iterable}也不是支持的{@code ResponseEntity}类型，则抛出此异常。
     */
    @Nullable
    protected Iterable<?> obtainExportData(@Nullable Object result) {
        if (result == null) {
            return null;
        }
        if (result instanceof Iterable<?> iterable) {
            return iterable;
        }
        else if (result instanceof HttpEntity<?> responseEntity) {
            if (!responseEntity.hasBody()) {
                return null;
            }
            if (responseEntity.getBody() instanceof Iterable<?> iterable) {
                return iterable;
            }
        }
        throw new IllegalArgumentException("response data type must be [? extends java.lang.Iterable] or ResponseEntity<? extends java.lang.Iterable<?>>");
    }


}
