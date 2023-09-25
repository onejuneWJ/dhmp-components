package com.zznode.dhmp.context.annotation;

import com.zznode.dhmp.context.ProvinceComponentScannerRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 启用省份自定义，扫描省份自定义组件
 *
 * <p>注意：请将此注解标记在启动类上，否则标记在有@Configuration注解的类上并必须指定basePackages
 *
 * @author 王俊
 * @date create in 2023/7/6 18:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ProvinceComponentScannerRegistrar.class)
public @interface ProvinceComponentScan {

    /**
     * @see #basePackages()
     */
    @AliasFor(attribute = "basePackages")
    String[] value() default {};


    /**
     * 将扫描
     *
     * @return 扫描包
     */
    @AliasFor(attribute = "value")
    String[] basePackages() default {};
}
