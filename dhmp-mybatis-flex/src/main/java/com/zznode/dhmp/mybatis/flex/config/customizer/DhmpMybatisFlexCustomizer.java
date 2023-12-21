package com.zznode.dhmp.mybatis.flex.config.customizer;

import com.mybatisflex.core.FlexGlobalConfig;
import org.springframework.core.Ordered;

/**
 * 允许定义多个个性化配置
 * MyBatisFlexCustomizer自动配置只有一个
 * @author 王俊
 * @see com.mybatisflex.spring.boot.MyBatisFlexCustomizer
 */
public interface DhmpMybatisFlexCustomizer extends Ordered, Comparable<DhmpMybatisFlexCustomizer> {
    /**
     * 自定义 MyBatis-Flex 配置。
     *
     * @param globalConfig 全局配置
     */
    void customize(FlexGlobalConfig globalConfig);

    @Override
    default int compareTo(DhmpMybatisFlexCustomizer other) {
        return getOrder() - other.getOrder();
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
