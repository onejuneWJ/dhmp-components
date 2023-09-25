package com.zznode.dhmp.export.converter;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 枚举转换
 *
 * @author 王俊
 * @date create in 2023/7/21 16:43
 */
public class EnumConverter<E extends Enum<E>> implements Converter {

    private final Class<E> enumClass;
    /**
     * 从枚举中进行比较获取枚举的字段
     */
    private String enumGetName;
    /**
     * 枚举转换后的值字段
     */
    private String enumValueName;

    /**
     * 对应枚举键值缓存
     */
    private final Map<E, Pair<Object, Object>> enumMap = new HashMap<>();

    public EnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    public void setEnumGetName(String enumGetName) {
        this.enumGetName = enumGetName;
    }

    public void setEnumValueName(String enumValueName) {
        this.enumValueName = enumValueName;
    }

    public void init() {
        Assert.hasText(enumGetName, "enumGetName cannot be empty");
        Assert.hasText(enumValueName, "enumValueName cannot be empty");
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            Object code = ReflectUtil.getFieldValue(e, enumGetName);
            Object name = ReflectUtil.getFieldValue(e, enumValueName);
            Pair<Object, Object> pair = new Pair<>(code, name);
            enumMap.put(e, pair);
        }
    }

    @Override
    public Object convert(Object value, Object data) {
        return enumMap.values().stream()
                .filter(pair -> Objects.equals(pair.getKey(), value))
                .map(Pair::getValue)
                .findFirst()
                .orElse(value);
    }

    @Override
    public boolean supports(Object value) {
        // 不允许在注解上使用
        return false;
    }

}
