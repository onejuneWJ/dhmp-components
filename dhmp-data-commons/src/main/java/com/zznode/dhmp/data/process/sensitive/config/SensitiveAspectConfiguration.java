package com.zznode.dhmp.data.process.sensitive.config;

import com.zznode.dhmp.data.process.sensitive.interceptor.SensitiveAdvisingBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.util.Assert;

/**
 * 脱敏配置
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SensitiveAspectConfiguration extends AbstractSensitiveConfiguration{


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SensitiveAdvisingBeanPostProcessor sensitiveAdvisingBeanPostProcessor() {
        Assert.state(this.enabledAttributes != null, "@EnableSensitiveAspect annotation metadata was not injected");
        SensitiveAdvisingBeanPostProcessor bpp = new SensitiveAdvisingBeanPostProcessor();
        bpp.configure(dataProcessor);
        bpp.setProxyTargetClass(this.enabledAttributes.getBoolean("proxyTargetClass"));
        bpp.setOrder(this.enabledAttributes.<Integer>getNumber("order"));
        return bpp;
    }

}
