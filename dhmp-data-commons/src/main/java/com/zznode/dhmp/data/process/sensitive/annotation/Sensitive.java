package com.zznode.dhmp.data.process.sensitive.annotation;

import java.lang.annotation.*;


/**
 * 被标记的方法返回的数据需要脱敏处理
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

}
