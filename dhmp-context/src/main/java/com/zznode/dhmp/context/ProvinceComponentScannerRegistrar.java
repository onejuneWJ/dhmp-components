package com.zznode.dhmp.context;

import com.zznode.dhmp.context.annotation.ProvinceComponentScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ImportBeanDefinitionRegistrar实现。用于注册ProvinceComponentScannerConfigurer
 *
 * @author 王俊
 * @date create in 2023/7/7 10:00
 * @see ProvinceComponentScannerConfigurer
 */
public class ProvinceComponentScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(ProvinceComponentScan.class.getName()));
        if (annotationAttributes != null) {
            registerBeanDefinitions(importingClassMetadata, annotationAttributes, registry,
                    generateBaseBeanName(importingClassMetadata));
        }
    }


    void registerBeanDefinitions(AnnotationMetadata annotationMetadata, AnnotationAttributes annotationAttributes,
                                 BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ProvinceComponentScannerConfigurer.class);

        List<String> basePackages = Arrays.stream(annotationAttributes.getStringArray("basePackages"))
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        if (basePackages.isEmpty()) {
            basePackages.add(getDefaultBasePackage(annotationMetadata));
        }
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

        // for spring-native
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata) {
        // 如：com.example.demo.DemoApplication#ProvinceCustomizerBeanScannerRegistrar#0
        return importingClassMetadata.getClassName() + "#" + ProvinceComponentScannerRegistrar.class.getSimpleName() + "#" + 0;
    }

    private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
        // 被标记的配置类的包路径，如com.example.demo.DemoApplication
        return ClassUtils.getPackageName(importingClassMetadata.getClassName());
    }
}
