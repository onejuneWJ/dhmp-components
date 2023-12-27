package com.zznode.dhmp.mybatis.flex.annotation;


import java.lang.annotation.*;

/**
 * 启用该注解，会自动填充审计字段
 * <p>被该注解标记的实体类需要继承至{@link com.zznode.dhmp.mybatis.flex.domain.BaseModel}
 *
 * @author 王俊
 * @date create in 2023/7/10 14:57
 * @see com.zznode.dhmp.mybatis.flex.domain.BaseModel
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModifyAudit {
}
