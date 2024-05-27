package com.zznode.dhmp.schedule.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * 描述
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class DhmpSchedulingConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public JobHandlerAnnotationBeanPostProcessor jobHandlerBeanPostProcessor() {
        return new JobHandlerAnnotationBeanPostProcessor();
    }
}
