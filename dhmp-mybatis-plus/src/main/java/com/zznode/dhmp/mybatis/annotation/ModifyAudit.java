package com.zznode.dhmp.mybatis.annotation;


import com.zznode.dhmp.mybatis.domain.BaseEntity;

import java.lang.annotation.*;

/**
 * 启用该注解，会自动填充审计字段
 * <p>被该注解标记的实体类需要继承至{@link BaseEntity}
 *
 * @author 王俊
 * @date create in 2023/7/10 14:57
 * @see BaseEntity
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModifyAudit {
}
