package com.zznode.dhmp.data.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 抽象一层
 *
 * @author 王俊
 */
public abstract class AbstractDataProcessor implements DataProcessor {

    protected final Log logger = LogFactory.getLog(getClass());

    static Set<Class<?>> skipClasses = Collections.newSetFromMap(new HashMap<>(16));

    private static final Class<?>[] CACHED_COMMON_TYPES = {
            boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class,
            double.class, Double.class, float.class, Float.class, int.class, Integer.class,
            long.class, Long.class, short.class, Short.class, String.class, Object.class};

    static {
        skipClasses.addAll(List.of(CACHED_COMMON_TYPES));
    }

    @Override
    public void process(@Nullable Object object) {
        if (object == null) {
            return;
        }
        Class<?> objectClass = object.getClass();
        if (shouldProcess(objectClass)) {

            if (object instanceof Iterable<?> iterable) {
                // 如果是可迭代类型，则遍历处理
                for (Object o : iterable) {
                    process(o);
                }
                return;
            }

            processInternal(object, objectClass);
        }
    }


    /**
     * 是否是可处理的类型
     *
     * @param objectClass 对象类
     * @return true 是
     */
    protected boolean shouldProcess(Class<?> objectClass) {
        return !isSimpleType(objectClass);
    }

    /**
     * 是否是简单类型。
     * <p>
     * 八大基础类型及其包装类型、Object类型
     *
     * @param objectClass 对象类
     * @return true 是
     */
    protected final boolean isSimpleType(Class<?> objectClass) {
        return objectClass.isPrimitive() || skipClasses.contains(objectClass);
    }

    protected abstract void processInternal(Object object, Class<?> objectClass);
}
