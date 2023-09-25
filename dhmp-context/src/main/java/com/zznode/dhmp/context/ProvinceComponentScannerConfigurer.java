package com.zznode.dhmp.context;

import com.zznode.dhmp.context.annotation.ProvinceComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/7/7 10:01
 */
public class ProvinceComponentScannerConfigurer implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware, BeanClassLoaderAware {

    String basePackage;

    private BeanFactory beanFactory;

    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();


    /**
     * spring会调用此方法
     *
     * @param basePackage 扫描包路径
     */
    @SuppressWarnings("unused")
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ProvinceComponentScanner scanner = new ProvinceComponentScanner(registry, (ListableBeanFactory) this.beanFactory);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ProvinceComponent.class));
        scanner.setIncludeAnnotationConfig(false);
        scanner.setClassLoader(classLoader);
        scanner.scan(
                StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
