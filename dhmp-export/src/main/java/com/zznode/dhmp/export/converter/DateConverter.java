package com.zznode.dhmp.export.converter;


import com.zznode.dhmp.core.constant.BaseConstants;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

/**
 * yyyy-MM-dd 格式
 *
 * @author 王俊
 * @date create in 2023/7/21 16:35
 * @see BaseConstants.Pattern#DATE
 */
public class DateConverter extends AbstractDateConverter {
    public DateConverter() {
        super(BaseConstants.Pattern.DATE);
    }

    @Override
    public boolean supports(Object value) {
        if (!super.supports(value)) {
            return false;
        }
        if (value instanceof TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH);
        }
        return true;
    }
}
