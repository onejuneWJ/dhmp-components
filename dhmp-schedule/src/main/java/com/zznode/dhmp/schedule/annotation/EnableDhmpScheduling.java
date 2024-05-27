package com.zznode.dhmp.schedule.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用定时任务
 *
 * @author 王俊
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DhmpSchedulingConfiguration.class)
public @interface EnableDhmpScheduling {
}
