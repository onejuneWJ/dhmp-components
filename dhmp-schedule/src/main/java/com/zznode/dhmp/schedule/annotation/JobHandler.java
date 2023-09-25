package com.zznode.dhmp.schedule.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 描述
 * @author 王俊
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobHandler {

    /**
     * @see #code()
     */
    @AliasFor(attribute = "code")
    String value() default "";

    /**
     * 任务编码
     */
    @AliasFor(attribute = "value")
    String code() default "";

    String cron() default "";


}
