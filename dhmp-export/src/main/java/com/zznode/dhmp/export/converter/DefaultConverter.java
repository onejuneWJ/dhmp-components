package com.zznode.dhmp.export.converter;

/**
 * 默认转换，啥都不干
 *
 * @author 王俊
 * @date create in 2023/7/19 16:41
 */
public class DefaultConverter implements Converter {

    @Override
    public Object convert(Object value, Object data) {
        return value;
    }

    @Override
    public boolean supports(Object value) {
        return false;
    }
}
