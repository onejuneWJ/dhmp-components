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
 * 实现BeanDefinitionRegistryPostProcessor，在{@link org.springframework.context.annotation.ConfigurationClassPostProcessor ConfigurationClassPostProcessor}处理完之后，
 * 扫描包中带有{@link ProvinceComponent @ProvinceComponent}注解的类，并注册到spring容器中。
 * 在此之前，大部分除了带有{@link ProvinceComponent @ProvinceComponent}注解bean都已经被注册到容器中。
 *
 * <p>
 * 注意，配置类不要使用{@link ProvinceComponent @ProvinceComponent}注解，因为{@link ProvinceComponentScanner}扫描完成的bean将直接注册到容器中，
 * 不会被{@link org.springframework.context.annotation.ConfigurationClassParser}处理，所以配置类中的bean配置或者其注解将会无效。
 * 配置类中请使用{@link com.zznode.dhmp.context.condition.ConditionalOnProvince @ConditionalOnProvince}
 *
 * @author 王俊
 * @date create in 2023/7/7 10:01
 * @see ProvinceComponentScanner
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
