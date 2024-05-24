package com.zznode.dhmp.jdbc.datasource.config;

import com.zznode.dhmp.jdbc.datasource.DefaultDynamicDataSourceProvider;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSource;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceProvider;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * 动态数据源配置
 *
 * @author 王俊
 */
public class DynamicDataSourceConfigurationSupport {

    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        return new DynamicDataSource(dynamicDataSourceProvider);
    }

    @Bean
    public DynamicDataSourceProvider dataSourceProvider() {
        DynamicDataSourceProvider dataSourceProvider = new DefaultDynamicDataSourceProvider();
        configurerDataSourceProvider(dataSourceProvider);
        return dataSourceProvider;
    }


    protected void configurerDataSourceProvider(DynamicDataSourceProvider dynamicDataSourceProvider) {

    }
}
