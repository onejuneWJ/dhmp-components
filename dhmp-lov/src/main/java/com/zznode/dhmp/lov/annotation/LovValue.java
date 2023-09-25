package com.zznode.dhmp.lov.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 值集翻译字段
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LovValue {

    @AliasFor(attribute = "lovCode")
    String value();

    @AliasFor(attribute = "value")
    String lovCode() default "";

    /**
     * 翻译值赋给字段
     */
    String titleField() default "";
}
