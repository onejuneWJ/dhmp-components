package com.zznode.dhmp.export.converter;

/**
 * 报表值转换器
 *
 * @author 王俊
 * @date create in 2023/7/19 13:56
 */
public interface Converter {

    /**
     * 转换值
     *
     * @param value 值
     * @param data  对象
     * @return 转换后的值
     */
    Object convert(Object value, Object data);

    /**
     * 是否支持转换该类型的值,避免不支持导致报错
     *
     * @param value 值
     * @return 为true才执行 {@link #convert}
     */
    boolean supports(Object value);
}
