package com.zznode.dhmp.export.config;

import com.zznode.dhmp.core.message.DhmpMessageSource;
import com.zznode.dhmp.export.interceptor.ExportBeanPostProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;

/**
 * 注册启用基于代理的导出方法执行所需的Spring基础设施bean
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ExportConfiguration extends AbstractExportConfiguration implements InitializingBean {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ExportBeanPostProcessor exportBeanPostProcessor(Environment environment) {
        ExportBeanPostProcessor processor = new ExportBeanPostProcessor();
        // 跟其他aop相关的配置一样
        boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
        processor.setProxyTargetClass(proxyTargetClass);
        processor.configure(exporterFactory);
        return processor;
    }

    @Override
    public void afterPropertiesSet() {
        DhmpMessageSource.addBasename("messages.messages-export");
    }
}
