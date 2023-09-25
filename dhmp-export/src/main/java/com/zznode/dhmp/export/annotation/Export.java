package com.zznode.dhmp.export.annotation;

import java.lang.annotation.*;

/**
 * 标注在controller的方法上
 *
 * @author 王俊
 * @date create in 2023/7/17 15:06
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Export {

    /**
     * 导出对象,导出数据的类型
     */
    Class<?> value() default Object.class;

}
