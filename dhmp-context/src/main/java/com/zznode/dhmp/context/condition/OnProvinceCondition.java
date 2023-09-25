package com.zznode.dhmp.context.condition;

import com.zznode.dhmp.core.constant.Province;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;

/**
 * 省份满足条件
 *
 * @author 王俊
 * @date create in 2023/7/5 18:17
 */
public class OnProvinceCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MergedAnnotation<ConditionalOnProvince> provinceMergedAnnotation = metadata.getAnnotations().get(ConditionalOnProvince.class);
        Province[] provinces = provinceMergedAnnotation.getEnumArray("provinces", Province.class);
        Province currentProvince = Province.currentProvince();
        if (provinces.length == 0) {
            // 没有标定特定的省份，表示全国版本，放行
            return true;
        }
        return CollectionUtils.containsAny(Arrays.asList(provinces), Collections.singleton(currentProvince));
    }
}
