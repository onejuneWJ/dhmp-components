package com.zznode.dhmp.mybatis.flex.listener.set;

import com.mybatisflex.annotation.SetListener;

/**
 * 描述
 *
 * @author 王俊
 */
public interface SupportedListener extends SetListener {
    /**
     * 实体类属性设置。
     *
     * @param entity   实体类
     * @param property 属性名
     * @param value    属性值
     * @return 实体类
     */
    boolean supports(Object entity, String property, Object value);
}
