package com.zznode.dhmp.jdbc.datasource.annotation;

import com.zznode.dhmp.jdbc.datasource.interceptor.ProxyDynamicDataSourceConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 启用动态数据源切面
 *
 * @author 王俊
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ProxyDynamicDataSourceConfigurationSelector.class})
public @interface EnableDynamicDataSourceAspect {

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;

    int order() default Ordered.LOWEST_PRECEDENCE - 1;
}
