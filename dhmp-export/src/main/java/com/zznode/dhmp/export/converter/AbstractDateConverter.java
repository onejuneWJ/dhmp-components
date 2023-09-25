package com.zznode.dhmp.export.converter;

import cn.hutool.core.date.DateUtil;
import com.zznode.dhmp.export.annotation.ReportColumn;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 抽象的日期转换器,子类传递pattern
 * <p>日期格式化建议使用 {@link ReportColumn#pattern()}, 避免写大量转换器子类
 *
 * @author 王俊
 * @date create in 2023/7/19 18:02
 */
public abstract class AbstractDateConverter implements Converter {

    private final String pattern;

    protected AbstractDateConverter(String pattern) {
        this.pattern = pattern;
    }


    @Override
    public String convert(Object value, Object data) {
        if (value instanceof TemporalAccessor temporalAccessor) {
            return DateTimeFormatter.ofPattern(pattern).format(temporalAccessor);
        }
        if (value instanceof Date date) {
            return DateUtil.format(date, pattern);
        }
        return value.toString();
    }

    @Override
    public boolean supports(Object value) {
        return value instanceof Date || value instanceof TemporalAccessor;
    }
}
