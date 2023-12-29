package com.zznode.dhmp.schedule.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 标记次注解，将类标记为任务执行类。
 * <p> 标记的类必须继承{@link com.zznode.dhmp.schedule.handler.AbstractJobHandler}
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

    /**
     * 执行器名称
     */
    String name() default "";
}
