package com.zznode.dhmp.data.process.sensitive.annotation;

import com.zznode.dhmp.data.process.sensitive.format.SensitiveFormatter;

import java.lang.annotation.*;

/**
 * 标记在字段上，表明该字段是敏感字段
 *
 * <p>
 * 标记在类或者方法中，让返回的数据中包含的敏感字段进行脱敏处理
 *
 * @author 王俊
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveField {

    /**
     * 是否启用。默认: 是。
     * <p>
     * 有些字段默认会进行脱敏处，这个属性可以让在某些类中的这些字段不做脱敏处理。
     */
    boolean enable() default true;


    /**
     * 脱敏格式
     * <p>
     * @see SensitiveFormatter
     */
    String pattern() default "";



}
