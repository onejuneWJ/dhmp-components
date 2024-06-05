package com.zznode.dhmp.data.process.sensitive.config;

import com.zznode.dhmp.data.process.sensitive.SensitiveDataProcessor;
import com.zznode.dhmp.data.process.sensitive.annotation.EnableSensitiveAspect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.function.SingletonSupplier;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 脱敏配置类抽象
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
public abstract class AbstractSensitiveConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes enabledAttributes;

    @Nullable
    protected Supplier<SensitiveDataProcessor> dataProcessor;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enabledAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableSensitiveAspect.class.getName()));
        if (this.enabledAttributes == null) {
            throw new IllegalArgumentException(
                    "@EnableSensitiveAspect is not present on importing class " + importMetadata.getClassName());
        }
    }

    /**
     * Collect any {@link SensitiveConfigurer} beans through autowiring.
     */
    @Autowired
    void setConfigurers(ObjectProvider<SensitiveConfigurer> configurers) {
        Supplier<SensitiveConfigurer> configurer = SingletonSupplier.of(() -> {
            List<SensitiveConfigurer> candidates = configurers.stream().toList();
            if (CollectionUtils.isEmpty(candidates)) {
                return null;
            }
            if (candidates.size() > 1) {
                throw new IllegalStateException("Only one SensitiveConfigurer may exist");
            }
            return candidates.get(0);
        });
        this.dataProcessor = adapt(configurer, SensitiveConfigurer::sensitiveDataProcessor);
    }

    private <T> Supplier<T> adapt(Supplier<SensitiveConfigurer> supplier, Function<SensitiveConfigurer, T> provider) {
        return () -> {
            SensitiveConfigurer configurer = supplier.get();
            return (configurer != null ? provider.apply(configurer) : null);
        };
    }
}
