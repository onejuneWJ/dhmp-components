package com.zznode.dhmp.export.converter;

import com.zznode.dhmp.export.annotation.ReportColumn.EnumConvert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EnumConverter缓存
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public final class EnumConverterCache {

    private static final Map<Class<?>, EnumConverter<?>> ENUM_CONVERTER_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static EnumConverter<? extends Enum<?>> getEnumConverter(EnumConvert enumConvert) {
        Class<? extends Enum> enumClass = enumConvert.enumClass();
        EnumConverter<?> enumConverter = ENUM_CONVERTER_MAP.get(enumClass);
        if (enumConverter == null) {
            enumConverter = new EnumConverter<>(enumClass);
            enumConverter.setEnumGetName(enumConvert.enumGetName());
            enumConverter.setEnumValueName(enumConvert.enumValueName());
            enumConverter.init();
            ENUM_CONVERTER_MAP.put(enumClass, enumConverter);
        }
        return enumConverter;
    }
}
