package com.zznode.dhmp.jdbc.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的数据源提供
 *
 * @author 王俊
 */
public class DefaultDynamicDataSourceProvider implements DynamicDataSourceProvider, InitializingBean {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private final Map<Object, Object> dataSources = new ConcurrentHashMap<>();

    @Override
    public Map<Object, Object> getDataSources() {
        return dataSources;
    }

    @Override
    public Object getDefaultDataSource() {
        return dataSources.get(DataSourceType.MASTER);
    }

    /**
     * 添加数据源类型
     *
     * @param dataSourceName 数据源类型名称。
     */
    public void addDataSource(String dataSourceName, Object dataSource) {
        Assert.hasText(dataSourceName, "dataSourceName cannot be null");
        Assert.notNull(dataSource, "dataSource cannot be null");
        if (this.dataSources.containsKey(dataSourceName)) {
            logger.info(String.format("already has a dataSource named with %s", dataSourceName));
            return;
        }
        this.dataSources.put(dataSourceName, dataSource);
    }

    @Override
    public void afterPropertiesSet() {
        Object dataSource = dataSources.get(DataSourceType.MASTER);
        if (dataSource == null) {
            throw new IllegalStateException("Failed to initialize a master dataSource. Please check your configuration.");
        }
    }
}
