package com.zznode.dhmp.jdbc.datasource.interceptor;

import com.zznode.dhmp.jdbc.datasource.annotation.EnableDynamicDataSourceAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * 抽象类
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
public abstract class AbstractDynamicDataSourceConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes enabledAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enabledAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableDynamicDataSourceAspect.class.getName()));
        if (this.enabledAttributes == null) {
            throw new IllegalArgumentException(
                    "@EnableAsync is not present on importing class " + importMetadata.getClassName());
        }
    }
}
