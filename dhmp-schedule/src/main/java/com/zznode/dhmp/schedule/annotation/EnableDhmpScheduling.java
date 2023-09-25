package com.zznode.dhmp.schedule.annotation;

import com.zznode.dhmp.schedule.ScheduleInit;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 描述
 *
 * @author 王俊
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ScheduleInit.class)
public @interface EnableDhmpScheduling {
}
