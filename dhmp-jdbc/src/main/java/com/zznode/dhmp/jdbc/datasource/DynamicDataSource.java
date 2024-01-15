package com.zznode.dhmp.jdbc.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @author 王俊
 * @date create in 2023/5/25 11:28
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements AutoCloseable {

    public DynamicDataSource(DataSourceProvider dataSourceProvider) {

        super.setDefaultTargetDataSource(dataSourceProvider.getMasterDataSource());
        super.setTargetDataSources(dataSourceProvider.getDataSources());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    @Override
    public void close() throws Exception {
        getResolvedDataSources()
                .values()
                .forEach(dataSource -> {
                    if (dataSource instanceof AutoCloseable autoCloseable) {
                        try {
                            autoCloseable.close();
                        } catch (Exception e) {
                            logger.error("error close data source", e);
                        }
                    }
                });
    }
}
