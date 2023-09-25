package com.zznode.dhmp.jdbc.datasource.annotation;

import com.zznode.dhmp.jdbc.datasource.DataSourceType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 用于某个方法或者类上，调用方法时，
 * 数据库操作将会启用{@link #dataSourceType} 类型的数据源
 * <p>注意
 * @apiNote 请将此注解标注在类上或者"外部调用"的方法上
 * @author 王俊
 * @date create in 2023/5/25 18:11
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseDynamicDataSource {

    @AliasFor(attribute = "dataSourceType")
    String value() default DataSourceType.MASTER;

    @AliasFor(attribute = "value")
    String dataSourceType() default DataSourceType.MASTER;
}
