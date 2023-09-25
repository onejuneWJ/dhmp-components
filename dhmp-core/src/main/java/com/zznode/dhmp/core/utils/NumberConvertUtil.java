package com.zznode.dhmp.core.utils;

/**
 * 数字强转工具
 * <p>通常是从json或者数据库中拿到的值,需要转换成具体的类型使用
 *
 * @author 王俊
 * @date create in 2023/8/4
 */
public class NumberConvertUtil {

    /**
     * 将已知数字类型但是被声明为{@link Object}类型转换成{@link Long}类型
     * <p>通常是从json或者数据库中拿到的值
     *
     * @param value object
     * @return {@link Long}类型的值
     * @throws NumberFormatException 值无法转换
     */
    public static Long convertToLong(Object value) throws NumberFormatException {
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
        try {
            return convertToLong(value);
        } catch (NumberFormatException e) {
            return 0L;
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
        try {
            return convertToInteger(value);
        } catch (NumberFormatException e) {
            return 0;
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
        try {
            return convertToDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
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
        try {
            return convertToFloat(value);
        } catch (NumberFormatException e) {
            return 0.0F;
        }
    }


}
