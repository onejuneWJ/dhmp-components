package com.zznode.dhmp.jdbc.datasource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据源map提供者
 *
 * @author 王俊
 * @date create in 2023/8/29
 */
public interface DataSourceProvider {

    /**
     * 获取数据源映射
     * @return 数据源映射map
     */
    Map<Object, Object> getDataSources();

    /**
     * 获取主要数据源
     * @return 主要数据源
     */
    DataSource getMasterDataSource();
}
