package com.zznode.dhmp.jdbc.datasource;

import java.util.Map;

/**
 * 数据源map提供者
 *
 * @author 王俊
 * @date create in 2023/8/29
 */
public interface DynamicDataSourceProvider {

    /**
     * 获取数据源映射
     *
     * @return 数据源映射map
     */
    Map<Object, Object> getDataSources();

    /**
     * 获取默认数据源
     *
     * @return 默认数据源
     */
    Object getDefaultDataSource();

    default void addDataSource(String dataSourceName, Object dataSource) {

    }
}
