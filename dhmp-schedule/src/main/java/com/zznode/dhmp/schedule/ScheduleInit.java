package com.zznode.dhmp.schedule;

import com.zznode.dhmp.schedule.annotation.JobHandler;
import com.zznode.dhmp.schedule.handler.AbstractJobHandler;
import com.zznode.dhmp.schedule.registry.JobHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * 初始化类
 *
 * @author 王俊
 */
public class ScheduleInit implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        registerAllJobHandlers();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 扫描并注册执行器
     */
    public void registerAllJobHandlers(){
        Map<String, Object> beansWithAnnotation = this.applicationContext.getBeansWithAnnotation(JobHandler.class);
        for (Object value : beansWithAnnotation.values()) {
            JobHandler annotation = AnnotationUtils.findAnnotation(value.getClass(), JobHandler.class);
            if (annotation == null) {
                continue;
            }
            String jobCode = annotation.code();
            if (value instanceof AbstractJobHandler jobHandler) {
                JobHandlerRegistry.registerJobHandler(jobCode, jobHandler);
            }
        }
    }
}
