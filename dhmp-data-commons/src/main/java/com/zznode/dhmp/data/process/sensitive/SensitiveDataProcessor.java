package com.zznode.dhmp.data.process.sensitive;

import com.zznode.dhmp.data.process.AbstractFieldAnnotatedDataProcessor;
import com.zznode.dhmp.data.process.sensitive.annotation.SensitiveField;
import com.zznode.dhmp.data.process.sensitive.format.FormatterRegistry;
import com.zznode.dhmp.data.process.sensitive.format.SensitiveFormatter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * 脱毛处理类
 *
 * @author 王俊
 */
public class SensitiveDataProcessor extends AbstractFieldAnnotatedDataProcessor<SensitiveField> {


    @Override
    protected boolean shouldProcess(Field field) {
        if (!super.shouldProcess(field)) {
            return false;
        }
        SensitiveField annotation = field.getAnnotation(SensitiveField.class);
        if (annotation != null && !annotation.enable()) {
            return false;
        }
        Class<?> type = field.getType();
        // 数据类型必须是String类型
        return String.class.isAssignableFrom(type);
    }

    @Override
    protected Object processFieldValue(String fieldName, Object fieldValue, @Nullable Field field) {
        if (fieldValue instanceof String str) {
            SensitiveFormatter formatter = determineFormatter(fieldName, field);
            if (formatter != null) {
                return formatter.format(str);
            }
        }
        return fieldValue;
    }

    @Nullable
    private SensitiveFormatter determineFormatter(String fieldName, @Nullable Field field) {
        if (field == null) {
            // 如果没有定义的字段对象，直接从缓存中获取对应字段的默认的formatter
            return FormatterRegistry.getFormatter(fieldName);
        }

        // 尝试从缓存中获取
        Class<?> declaringClass = field.getDeclaringClass();
        SensitiveFormatter formatter = FormatterRegistry.getFormatter(fieldName, declaringClass);
        if (formatter != null) {
            return formatter;
        }

        // 如果没有获取到formatter,从定义的字段注解上获取
        SensitiveField annotation = field.getAnnotation(SensitiveField.class);
        if (annotation == null) {
            // 如果字段上没有注解，可能是默认需要脱敏的字段，再次尝试从缓存中获取。
            return FormatterRegistry.getFormatter(fieldName);
        }
        formatter = fromAnnotation(annotation);
        if (formatter != null) {
            FormatterRegistry.registerFormatter(fieldName, declaringClass, formatter);
            return  formatter;
        }
        return null;
    }


    /**
     * 从注解中获取敏感信息格式化器。
     * <p>此方法尝试根据注解中指定的格式创建一个{@link SensitiveFormatter}实例。如果注解中没有指定格式，
     * 或者指定的格式无效，则返回null。</p>
     *
     * @param annotation 分配给字段的{@link SensitiveField}注解实例。
     * @return 根据注解中的格式创建的{@link SensitiveFormatter}实例，如果未指定格式则返回null。
     */
    @Nullable
    private SensitiveFormatter fromAnnotation(SensitiveField annotation) {
        // 尝试获取注解中指定的格式
        String pattern = annotation.pattern();
        if (!StringUtils.hasText(pattern)) {
            // 如果未指定格式，返回null
            return null;
        }
        // 根据获取到的格式创建并返回敏感信息格式化器
        return SensitiveFormatter.ofPattern(pattern);
    }

}
