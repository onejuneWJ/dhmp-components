package com.zznode.dhmp.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数字强转工具
 * <p>通常是从json或者数据库中拿到的值,需要转换成具体的类型使用
 *
 * @author 王俊
 * @date create in 2023/8/4
 */
public class NumberConvertUtil {

    private static final Logger logger = LoggerFactory.getLogger(NumberConvertUtil.class);

    /**
     * 将已知数字类型但是被声明为{@link Object}类型转换成{@link Long}类型
     * <p>通常是从json或者数据库中拿到的值
     *
     * @param value object
     * @return {@link Long}类型的值
     * @throws NumberFormatException 值无法转换
     */
    public static Long convertToLong(Object value) throws NumberFormatException {
        return convertToLong(value, 0);
    }

    public static Long convertToLong(Object value, long defaultValue) throws NumberFormatException {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String s) {
            return Long.valueOf(s);
        }
        throw new NumberFormatException("value can't be converted to long");
    }

    /**
     * 安全的转换方法,返回默认值
     *
     * @param value object
     * @return {@link Long}类型的值
     */
    public static Long convertToLongSafe(Object value) {
        return convertToLongSafe(value, 0L);
    }

    public static Long convertToLongSafe(Object value, long defaultValue) {
        try {
            return convertToLong(value, defaultValue);
        } catch (NumberFormatException e) {
            logger.warn("Error converting value to Long, fallback to default value {}", defaultValue);
            return defaultValue;
        }
    }

    /**
     * 将已知数字类型但是被声明为{@link Object}类型转换成{@link Integer}类型
     * <p>通常是从json或者数据库中拿到的值
     *
     * @param value object
     * @return {@link Integer}类型的值
     * @throws NumberFormatException 值无法转换
     */
    public static Integer convertToInteger(Object value) throws NumberFormatException {
        return convertToInteger(value, 0);
    }

    public static Integer convertToInteger(Object value, int defaultValue) throws NumberFormatException {
        if (null == value) {
            return defaultValue;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String s) {
            return Integer.valueOf(s);
        }
        throw new NumberFormatException("value can't be converted to Integer");
    }

    /**
     * 安全的转换方法,返回默认值
     *
     * @param value object
     * @return {@link Integer}类型的值
     */
    public static Integer convertToIntegerSafe(Object value) {
        return convertToIntegerSafe(value, 0);
    }

    public static Integer convertToIntegerSafe(Object value, int defaultValue) {
        try {
            return convertToInteger(value, defaultValue);
        } catch (NumberFormatException e) {
            logger.warn("Error converting value to Integer, fallback to default value {}", defaultValue);
            return defaultValue;
        }
    }

    /**
     * 将已知数字类型但是被声明为{@link Object}类型转换成{@link Double}类型
     * <p>通常是从json或者数据库中拿到的值
     *
     * @param value object
     * @return {@link Double}类型的值
     * @throws NumberFormatException 值无法转换
     */
    public static Double convertToDouble(Object value) throws NumberFormatException {
        return convertToDouble(value, 0.0D);
    }

    public static Double convertToDouble(Object value, double defaultValue) throws NumberFormatException {
        if (null == value) {
            return defaultValue;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value instanceof String s) {
            return Double.valueOf(s);
        }
        throw new NumberFormatException("value can't be converted to Double");
    }

    /**
     * 安全的转换方法,返回默认值
     *
     * @param value object
     * @return {@link Double}类型的值
     */
    public static Double convertToDoubleSafe(Object value) {
        return convertToDoubleSafe(value, 0.0D);
    }

    public static Double convertToDoubleSafe(Object value, double defaultValue) {
        try {
            return convertToDouble(value, defaultValue);
        } catch (NumberFormatException e) {
            logger.warn("Error converting value to Double, fallback to default value {}", defaultValue);
            return defaultValue;
        }
    }

    /**
     * 将已知数字类型但是被声明为{@link Object}类型转换成{@link Float}类型
     * <p>通常是从json或者数据库中拿到的值
     *
     * @param value object
     * @return {@link Double}类型的值
     * @throws NumberFormatException 值无法转换
     */
    public static Float convertToFloat(Object value) throws NumberFormatException {
        return convertToFloat(value, 0);
    }

    public static Float convertToFloat(Object value, float defaultValue) throws NumberFormatException {
        if (null == value) {
            return defaultValue;
        }

        if (value instanceof Number number) {
            return number.floatValue();
        }
        if (value instanceof String s) {
            return Float.valueOf(s);
        }
        throw new NumberFormatException("value can't be converted to Double");
    }

    /**
     * 安全的转换方法,返回默认值
     *
     * @param value object
     * @return {@link Float}类型的值
     */
    public static Float convertToFloatSafe(Object value) {
        return convertToFloatSafe(value, 0.0F);
    }

    public static Float convertToFloatSafe(Object value, float defaultValue) {
        try {
            return convertToFloat(value, defaultValue);
        } catch (NumberFormatException e) {
            logger.warn("Error converting value to Float, fallback to default value {}", defaultValue);
            return defaultValue;
        }
    }


}
