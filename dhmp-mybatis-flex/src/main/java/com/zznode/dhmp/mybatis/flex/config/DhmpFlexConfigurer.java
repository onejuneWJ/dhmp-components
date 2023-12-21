package com.zznode.dhmp.mybatis.flex.config;

import com.mybatisflex.annotation.SetListener;
import com.zznode.dhmp.mybatis.flex.config.customizer.DhmpMybatisFlexCustomizer;

import java.util.List;

/**
 * mybatis-flex配置器
 *
 * @author 王俊
 */
public interface DhmpFlexConfigurer {

    /**
     * 自定义添加setListeners
     *
     * @param setListeners setListeners
     */
    default void addSetListeners(List<SetListener> setListeners) {
    }

    /**
     * 添加自定义DhmpMyBatisFlexCustomizer
     * @param customizerList customizerList
     */
    default void addCustomizers(List<DhmpMybatisFlexCustomizer> customizerList) {
    }


}
