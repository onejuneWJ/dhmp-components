package com.zznode.dhmp.data.process.sensitive.format;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字段对应的脱敏格式化类的缓存。
 *
 * @author 王俊
 */
public class FormatterRegistry {

    private static final Map<String, SensitiveFormatter> FIELD_NAME_CACHE = new ConcurrentHashMap<>(64);

    private static final Map<CacheKey, SensitiveFormatter> FIELD_CLASS_CACHE = new ConcurrentHashMap<>(64);

    static {
        //  注册默认的脱敏格式化类
        registerFormatter("account", SensitiveFormatter.ACCOUNT_FORMATTER);
        registerFormatter("email", SensitiveFormatter.EMAIL_FORMATTER);
        registerFormatter("username", SensitiveFormatter.ofPattern(SensitiveFormatter.CHINESE_NAME));
    }

    public static void registerFormatter(String fieldName, SensitiveFormatter formatter) {
        Assert.notNull(formatter, "formatter cannot be null.");
        FIELD_NAME_CACHE.put(fieldName, formatter);
    }

    public static void registerFormatter(String fieldName, Class<?> objectClass, SensitiveFormatter formatter) {
        Assert.notNull(formatter, "formatter cannot be null.");
        FIELD_CLASS_CACHE.put(new CacheKey(fieldName, objectClass), formatter);
    }

    @Nullable
    public static SensitiveFormatter getFormatter(String fieldName) {
        return FIELD_NAME_CACHE.get(fieldName);
    }

    @Nullable
    public static SensitiveFormatter getFormatter(String fieldName, @Nullable Class<?> objectClass) {
        if (objectClass == null) {
            return getFormatter(fieldName);
        }
        return FIELD_CLASS_CACHE.get(new CacheKey(fieldName, objectClass));
    }


    private record CacheKey(String fieldName, @Nullable Class<?> targetClass) {


        @Override
        public boolean equals(@Nullable Object other) {
            return (this == other || (other instanceof CacheKey that &&
                    this.fieldName.equals(that.fieldName) &&
                    ObjectUtils.nullSafeEquals(this.targetClass, that.targetClass)));
        }

        @Override
        public String toString() {
            return this.fieldName + (this.targetClass != null ? " on " + this.targetClass : "");
        }

    }
}
