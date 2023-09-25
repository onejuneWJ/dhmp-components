package com.zznode.dhmp.context.condition;

import com.zznode.dhmp.context.annotation.ProvinceComponent;
import com.zznode.dhmp.core.constant.Province;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * {@link ProvinceComponent} 局限性太强，
 * 只能排查componentScan扫描的类，不能在配置类中声明bean时使用，遂增加此类
 *
 * @author 王俊
 * @date create in 2023/7/5 18:18
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnProvinceCondition.class)
public @interface ConditionalOnProvince {

    @AliasFor(attribute = "provinces")
    Province[] value() default {};

    @AliasFor(attribute = "value")
    Province[] provinces() default {};
}
