package com.zznode.dhmp.context;

import cn.hutool.core.collection.ConcurrentHashSet;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.util.ClassUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 继承至{@link ClassPathBeanDefinitionScanner}, 重写了bean扫描
 *
 * @author 王俊
 * @date create in 2023/5/15 15:14
 */
public final class ProvinceComponentScanner extends ClassPathBeanDefinitionScanner {


    private final BeanDefinitionRegistry registry;

    private final ListableBeanFactory beanFactory;


    private final Set<String> removedSupperBeans = new ConcurrentHashSet<>(8);

    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    // 使用 ConcurrentHashMap 来保证线程安全，并发性能更好
    private static final ConcurrentHashMap<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

    public ProvinceComponentScanner(BeanDefinitionRegistry registry, ListableBeanFactory beanFactory) {
        super(registry, false);
        this.registry = registry;
        this.beanFactory = beanFactory;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public int scan(String... basePackages) {
        logger.info(String.format("scanning province customizer component in packages %s", Arrays.toString(basePackages)));
        long startTime = System.nanoTime();
        int count = super.scan(basePackages);
        Duration duration = Duration.ofNanos(System.nanoTime() - startTime);
        logger.info(String.format("Finished ProvinceComponent scanning in %s ms.", duration.toMillis()));
        return count;
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            logger.warn("No ProvinceComponent was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
            return Collections.emptySet();
        }
        return beanDefinitionHolders;
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        if (!super.checkCandidate(beanName, beanDefinition)) {
            return false;
        }

        String beanClassName = beanDefinition.getBeanClassName();
        if (beanClassName == null) {
            return false;
        }
        if (RegisteredBeansRegistry.contains(beanClassName)) {
            return false;
        }
        // 使用缓存来避免重复解析类名
        Class<?> beanClass = CLASS_CACHE.computeIfAbsent(beanClassName, (k) -> ClassUtils.resolveClassName(k, classLoader));
        Class<?> superclass = beanClass.getSuperclass();
        // 找到父类定义的bean, 并移除
        checkAndRemoveParentBean(superclass);
        RegisteredBeansRegistry.add(beanClassName);
        return true;
    }

    /**
     * 检查父类是否是一个具体的类，并且存在于bean容器中。如果存在则从容器中移除。
     *
     * @param superclass 父类class
     */
    private void checkAndRemoveParentBean(Class<?> superclass) {
        String superclassName = superclass.getName();
        if (superclass.equals(Object.class) || removedSupperBeans.contains(superclassName)) {
            return;
        }
        String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, superclass);

        Arrays.stream(beanNames)
                .filter(beanName -> isBeanDefinitionMatch(superclass, beanName))
                .forEach(beanName -> {
                    this.registry.removeBeanDefinition(beanName);
                    removedSupperBeans.add(superclassName);
                });
        RegisteredBeansRegistry.add(superclassName);
        // 递归移除父类的父类
        // 省份自定义可能存在多重继承
        checkAndRemoveParentBean(superclass.getSuperclass());
    }

    /**
     * 检查给定beanName对应的BeanDefinition是否与指定的superclass匹配。
     *
     * @param superclass 父类class
     * @param beanName   bean名称
     * @return 是否匹配
     */
    private boolean isBeanDefinitionMatch(Class<?> superclass, String beanName) {
        BeanDefinition superBeanDefinition = this.registry.getBeanDefinition(beanName);
        BeanDefinition originatingBeanDefinition = superBeanDefinition.getOriginatingBeanDefinition();
        if (originatingBeanDefinition != null) {
            // 找到的bean是proxyDefinition，比如父类使用了Scope注解并设置代理
            // 不用考虑aop代理，因为还没到那里去
            superBeanDefinition = originatingBeanDefinition;
        }
        // beanClass必须是父类自己本身，(只移除父类本身的bean，避免误删)
        return superclass.getName().equals(superBeanDefinition.getBeanClassName());
    }
}
