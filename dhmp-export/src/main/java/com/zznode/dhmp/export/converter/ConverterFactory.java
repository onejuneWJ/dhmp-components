package com.zznode.dhmp.export.converter;

import org.springframework.beans.BeanUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link Converter}的工厂类
 *
 * @author 王俊
 * @date create in 2023/7/19 16:53
 */
public class ConverterFactory {

    private static final Map<Class<? extends Converter>, Converter> CONVERTER_CACHE = new ConcurrentHashMap<>(2);

    public static Converter getInstance(Class<? extends Converter> converterClass) {
        Converter converter = CONVERTER_CACHE.get(converterClass);
        if (converter == null) {
            converter = BeanUtils.instantiateClass(converterClass);
            CONVERTER_CACHE.put(converterClass, converter);
        }
        return converter;
    }
}
