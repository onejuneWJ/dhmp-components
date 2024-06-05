package com.zznode.dhmp.data.process.sensitive.interceptor;

import com.zznode.dhmp.data.process.sensitive.SensitiveDataProcessor;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.util.function.Supplier;

/**
 * bean 后置处理器，创建代理
 *
 * @author 王俊
 */
public class SensitiveAdvisingBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {
    @Serial
    private static final long serialVersionUID = 2148578547631847849L;

    @Nullable
    private Supplier<SensitiveDataProcessor> sensitiveDataProcessor;

    public void configure(@Nullable Supplier<SensitiveDataProcessor> sensitiveDataProcessor) {
        this.sensitiveDataProcessor = sensitiveDataProcessor;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.advisor = new SensitiveAdvisor(sensitiveDataProcessor);
    }


}
