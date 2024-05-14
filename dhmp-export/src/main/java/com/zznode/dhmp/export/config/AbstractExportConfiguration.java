package com.zznode.dhmp.export.config;

import com.zznode.dhmp.export.support.exporter.ExporterFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.function.SingletonSupplier;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 抽象的基本配置类
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
public abstract class AbstractExportConfiguration {
    @Nullable
    protected Supplier<ExporterFactory> exporterFactory;

    /**
     * 自动注入ExportConfigurer
     */
    @Autowired
    void setConfigurers(ObjectProvider<ExportConfigurer> configurers) {
        Supplier<ExportConfigurer> configurer = SingletonSupplier.of(() -> {
            List<ExportConfigurer> candidates = configurers.stream().toList();
            if (CollectionUtils.isEmpty(candidates)) {
                return null;
            }
            if (candidates.size() > 1) {
                throw new IllegalStateException("Only one ExportConfigurer may exist");
            }
            return candidates.get(0);
        });
        this.exporterFactory = adapt(configurer, ExportConfigurer::getExporterFactory);
    }

    private <T> Supplier<T> adapt(Supplier<ExportConfigurer> supplier, Function<ExportConfigurer, T> provider) {
        return () -> {
            ExportConfigurer configurer = supplier.get();
            return (configurer != null ? provider.apply(configurer) : null);
        };
    }
}
