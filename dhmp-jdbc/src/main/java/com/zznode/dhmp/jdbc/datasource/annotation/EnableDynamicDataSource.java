package com.zznode.dhmp.jdbc.datasource.annotation;

import com.zznode.dhmp.jdbc.datasource.config.DelegatingDynamicDataSourceConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用动态数据源
 *
 * @author 王俊
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DelegatingDynamicDataSourceConfiguration.class})
public @interface EnableDynamicDataSource {
}
