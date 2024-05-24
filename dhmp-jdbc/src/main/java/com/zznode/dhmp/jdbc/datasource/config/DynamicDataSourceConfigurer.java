package com.zznode.dhmp.jdbc.datasource.config;

import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceProvider;

/**
 * 自定义配置
 *
 * @author 王俊
 */
public interface DynamicDataSourceConfigurer {

    default void configureDataSourceProvider(DynamicDataSourceProvider dynamicDataSourceProvider){

    }
}
