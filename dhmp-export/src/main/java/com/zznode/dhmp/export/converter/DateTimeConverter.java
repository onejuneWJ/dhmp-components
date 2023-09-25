package com.zznode.dhmp.export.converter;


import com.zznode.dhmp.core.constant.BaseConstants;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

/**
 * yyyy-MM-dd HH:mm:ss 格式
 *
 * @author 王俊
 * @date create in 2023/7/20 18:08
 * @see BaseConstants.Pattern#DATETIME
 */
public class DateTimeConverter extends AbstractDateConverter {

    public DateTimeConverter() {
        super(BaseConstants.Pattern.DATETIME);
    }

    @Override
    public boolean supports(Object value) {
        if (!super.supports(value)) {
            return false;
        }
        if (value instanceof TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.SECOND_OF_MINUTE);
        }
        return true;
    }
}
