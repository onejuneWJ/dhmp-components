package com.zznode.dhmp.jdbc.datasource.interceptor;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.util.Assert;

/**
 * 配置类
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProxyDynamicDataSourceConfiguration extends AbstractDynamicDataSourceConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DynamicDataSourceBeanPostProcessor dynamicDataSourceBeanPostProcessor() {
        DynamicDataSourceBeanPostProcessor bpp = new DynamicDataSourceBeanPostProcessor();
        Assert.state(this.enabledAttributes != null, "@EnableDynamicDataSourceAspect annotation metadata was not injected");
        bpp.setProxyTargetClass(this.enabledAttributes.getBoolean("proxyTargetClass"));
        bpp.setOrder(this.enabledAttributes.<Integer>getNumber("order"));
        return bpp;
    }
}
