package com.zznode.dhmp.jdbc.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @author 王俊
 * @date create in 2023/5/25 11:28
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public DynamicDataSource(DataSourceProvider dataSourceProvider) {

        super.setDefaultTargetDataSource(dataSourceProvider.getMasterDataSource());
        super.setTargetDataSources(dataSourceProvider.getDataSources());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
