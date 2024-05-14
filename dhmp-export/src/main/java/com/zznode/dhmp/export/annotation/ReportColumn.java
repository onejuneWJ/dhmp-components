package com.zznode.dhmp.export.annotation;


import com.zznode.dhmp.export.converter.Converter;
import com.zznode.dhmp.export.converter.DefaultConverter;

import java.lang.annotation.*;

/**
 * @author 王俊
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReportColumn {

    /**
     * Excel列标题名称，必填
     */
    String value();

    /**
     * 字段值格式化模板. 可以是时间格式化, 也可以是数字格式化.
     * <p>只有可格式化类型才能格式化,其余类型数据添加此参数只会转换为string类型数据
     * <p>long类型的数据过长导致excel显示不友好,建议添加此参数(如##)
     *
     * @see java.text.DecimalFormat
     */
    String pattern() default "";

    /**
     * 值替换字段名(可选)
     */
    String replaceField() default "";

    /**
     * 直接使用代替字段, 不做其他处理
     */
    boolean forceReplace() default false;

    /**
     * 列排序
     */
    int order() default 0;

    /**
     * 前端默认选中
     */
    boolean defaultSelected() default true;

    /**
     * 这样做好像不灵活
     * 值的转换器class,优先级高于{@link #pattern}
     */
    Class<? extends Converter> converter() default DefaultConverter.class;

    /**
     * 值集编码
     * <p>用于值集翻译
     */
    String lovCode() default "";

    /**
     * 枚举转换
     */
    EnumConvert enumConvert() default @EnumConvert();


    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface EnumConvert {

        Class<? extends Enum> enumClass() default Enum.class;

        String enumGetName() default "";

        String enumValueName() default "";
    }

}
