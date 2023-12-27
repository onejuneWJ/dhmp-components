package com.zznode.dhmp.context;


import java.util.HashSet;
import java.util.Set;

/**
 * 已注册的省份组件, 不同的{@link ProvinceComponentScanner scanner}可以通过此类获取到容器中已存在的组件, 避免重复注册组件
 *
 * @author 王俊
 * @date create in 2023/8/1 18:14
 */
public final class RegisteredBeansRegistry {

    private static final Set<String>  REGISTERED_BEANS = new HashSet<>(8);

    public static void add(String beanClassName) {
        REGISTERED_BEANS.add(beanClassName);
    }

    public static boolean contains(String beanClassName) {
        return REGISTERED_BEANS.contains(beanClassName);
    }
}
