package com.zznode.dhmp.lov.annotation;

import java.lang.annotation.*;

/**
 * 值集翻译字段
 *
 * <p>需要mybatis增强组件支持
 * @author 王俊
 * @date create in 2023/8/31
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LovValue {

    /**
     * LOV编码
     */
    String value();

    /**
     * 翻译值赋给字段,如果不填,则不会进行翻译
     */
    String titleField() default "";
}
