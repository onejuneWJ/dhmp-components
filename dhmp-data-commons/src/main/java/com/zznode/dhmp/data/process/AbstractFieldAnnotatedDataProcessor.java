package com.zznode.dhmp.data.process;

import org.springframework.core.GenericTypeResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 处理带有特定注解的字段
 *
 * @author 王俊
 */
public abstract class AbstractFieldAnnotatedDataProcessor<A extends Annotation> extends AbstractDataProcessor {

    @SuppressWarnings("unchecked")
    private final Class<A> annotationType = (Class<A>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractFieldAnnotatedDataProcessor.class);

    /**
     * 默认处理的字段名集合。
     */
    private final Set<String> defaultProcessFieldNames = new HashSet<>();

    public void setDefaultProcessFieldNames(Set<String> fieldNames) {
        if (fieldNames != null) {
            this.defaultProcessFieldNames.addAll(fieldNames);
        }
    }

    @SuppressWarnings("unchecked")
    protected void processInternal(Object object, Class<?> objectClass) {

        if (object instanceof Map) {
            Map<Object, Object> m = (Map<Object, Object>) object;
            for (Map.Entry<Object, Object> entry : m.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key instanceof String fieldName) {
                    if (defaultProcessFieldNames.contains(fieldName) && isSimpleType(value.getClass())) {
                        Object processed = processFieldValue(fieldName, value, null);
                        entry.setValue(processed);
                    }
                }
            }
            return;
        }

        ReflectionUtils.doWithFields(objectClass,
                field -> processField(field, object),
                this::shouldProcess
        );
    }

    /**
     * 判断字段是否需要处理。
     *
     * @param field 要检查的字段。
     * @return 如果字段名包含在默认处理字段名集合中，或者字段上存在指定的注解，则返回true；否则返回false。
     */
    protected boolean shouldProcess(Field field) {
        // 检查字段名是否包含在默认处理字段名集合中
        boolean contains = defaultProcessFieldNames.contains(field.getName());
        // 检查字段上是否存在指定的注解
        boolean annotationPresent = field.isAnnotationPresent(annotationType);
        // 如果字段名包含在默认处理集合中或字段上有指定注解，则返回true
        return contains || annotationPresent;
    }


    protected void processField(Field field, Object object) {

        field.setAccessible(true);

        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, object);
        if (value == null) {

            return;
        }
        if (!isSimpleType(value.getClass())) {
            // 递归处理字段
            process(value);
            return;
        }

        ReflectionUtils.setField(field, object, processFieldValue(field.getName(), value, field));

    }


    /**
     * 处理字段的值
     *
     * @param fieldName 字段名称
     * @param value     字段值
     * @return 处理后的值
     */
    protected abstract Object processFieldValue(String fieldName, Object value, @Nullable Field field);
}
