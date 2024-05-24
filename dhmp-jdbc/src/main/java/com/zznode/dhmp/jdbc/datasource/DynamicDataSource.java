package com.zznode.dhmp.jdbc.datasource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.BeanFactoryDataSourceLookup;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * 动态数据源
 *
 * @author 王俊
 * @date create in 2023/5/25 11:28
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements BeanFactoryAware, AutoCloseable {

    /**
     * 提供动态数据源列表
     */
    private final DynamicDataSourceProvider dynamicDataSourceProvider;

    public DynamicDataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        Assert.notNull(dynamicDataSourceProvider, "DataSourceProvider must not be null");
        this.dynamicDataSourceProvider = dynamicDataSourceProvider;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    @Override
    public void close() {
        Collection<DataSource> dataSources = getResolvedDataSources().values();
        for (DataSource ds : dataSources) {
            if (ds instanceof AutoCloseable autoCloseable) {
                try {
                    autoCloseable.close();
                } catch (Exception ex) {
                    if (logger.isWarnEnabled()) {
                        String msg = "Invocation of close method failed on bean with name '" + ds + "'";
                        if (logger.isDebugEnabled()) {
                            // Log at warn level like below but add the exception stacktrace only with debug level
                            logger.warn(msg, ex);
                        }
                        else {
                            logger.warn(msg + ": " + ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() {

        super.setTargetDataSources(dynamicDataSourceProvider.getDataSources());
        super.setDefaultTargetDataSource(dynamicDataSourceProvider.getDefaultDataSource());
        super.afterPropertiesSet();
    }

    /**
     * 此方法执行在{@link #afterPropertiesSet()}之前
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        super.setDataSourceLookup(new BeanFactoryDataSourceLookup(beanFactory));
    }
}
