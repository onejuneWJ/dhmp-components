package com.zznode.dhmp.jdbc.datasource.config;

import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置代理，通过注解的方式启用动态数据源
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
public class DelegatingDynamicDataSourceConfiguration extends DynamicDataSourceConfigurationSupport {

    private final List<DynamicDataSourceConfigurer> configurers = new ArrayList<>();

    @Autowired(required = false)
    public void setConfigurers(List<DynamicDataSourceConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addAll(configurers);
        }
    }

    @Override
    protected void configurerDataSourceProvider(DynamicDataSourceProvider dynamicDataSourceProvider) {
        for (DynamicDataSourceConfigurer configurer : configurers) {
            configurer.configureDataSourceProvider(dynamicDataSourceProvider);
        }
    }
}
