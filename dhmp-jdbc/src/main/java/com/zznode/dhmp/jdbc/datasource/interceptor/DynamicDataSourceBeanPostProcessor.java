package com.zznode.dhmp.jdbc.datasource.interceptor;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

import java.io.Serial;

/**
 * bean后置处理器，用于拦截所有bean，并判断是否需要使用动态数据源
 *
 * @author 王俊
 */
public class DynamicDataSourceBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {
    @Serial
    private static final long serialVersionUID = 7020234481787711144L;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.advisor = new DynamicDataSourceAdvisor();
    }
}
