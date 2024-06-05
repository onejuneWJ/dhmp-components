package com.zznode.dhmp.data.process.sensitive.annotation;

import com.zznode.dhmp.data.process.sensitive.config.SensitiveAspectConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 启用数据脱敏处理切面
 *
 * @author 王俊
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SensitiveAspectConfiguration.class)
public @interface EnableSensitiveAspect {

    /**
     * 指示是否要创建基于子类(CGLIB)的代理，而不是基于标准Java接口的代理.
     */
    boolean proxyTargetClass() default false;

    /**
     * 拦截器的顺序。
     * <p>
     * 尽量在后面一点处理，顺序越后，越先执行，拿到的数据就是最新的
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
