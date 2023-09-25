package com.zznode.dhmp.export.annotation;

import java.lang.annotation.*;

/**
 * 标注在导出的类上，
 *
 * @author wangjun
 * @date 2020/9/11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReportSheet {

    /**
     * 导出文件名称、sheet页名称
     */
    String value();


    /**
     * 是否包含后缀. 如 [{@link #value}-suffix]
     */
    boolean suffixFlag() default true;

    /**
     * 后缀
     */
    Suffix suffix() default @Suffix();


    enum SuffixType {
        /**
         * 后缀类型, yyyyMMddHHmmss
         */
        DATE,
        /**
         * UUID作为后缀
         */
        UUID,
        /**
         * 自定义后缀
         */
        CUSTOM
    }

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Suffix {

        /**
         * 后缀类型
         */
        SuffixType suffixType() default SuffixType.DATE;

        /**
         * 后缀，当{@link #suffixType}为CUSTOM时生效
         */
        String customSuffix() default "";
    }
}
