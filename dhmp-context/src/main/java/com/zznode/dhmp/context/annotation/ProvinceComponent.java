package com.zznode.dhmp.context.annotation;

import com.zznode.dhmp.context.ProvinceComponentScanner;
import com.zznode.dhmp.context.condition.ConditionalOnProvince;
import com.zznode.dhmp.core.constant.Province;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 代替@Component注解，该组件将从Spring的组件扫描中排除, 并会重新被注册对应省份的组件实现。
 * <p>
 * 注意:
 * <li>该注解只会在{@link ProvinceComponentScanner ProvinceComponentScanner}扫描到的组件生效，其他bean声明方式请使用{@link ConditionalOnProvince}</li>
 * <li>
 * 如果此类标记的父类，在配置类({@link org.springframework.context.annotation.Configuration @Configuration} 类)中被声明为bean，由于优先级问题，在扫描组件的时候，找不到父类定义的bean，则不会移除父类定义的bean。
 * <p>此时需要将此组件使用{@link org.springframework.context.annotation.Primary @Primary}标记
 * </li>
 * <li>
 * 如果在开发中出现bean冲突问题(极少可能性会出现这种情况),建议在最底层的实现类中添加{@link org.springframework.context.annotation.Primary @Primary}注解
 * <p>出现这种情况
 * </li>
 *
 * @author 王俊
 * @date create in 2023/5/12 14:24
 * @see ProvinceComponentScan
 * @see ProvinceComponentScanner
 * @see ConditionalOnProvince
 * @see Component
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@ConditionalOnProvince
public @interface ProvinceComponent {

    /**
     * @return 组件名称
     * @see Component#value()
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * 当省份对应时才被注册，可填多个省份
     * <p>默认:空，表示全国
     */
    @AliasFor(annotation = ConditionalOnProvince.class, attribute = "provinces")
    Province[] provinces() default {};


}
