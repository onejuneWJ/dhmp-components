package com.zznode.dhmp.jdbc.datasource;

import org.springframework.util.Assert;

/**
 * 动态数据源上下文
 *
 * @author 王俊
 * @date create in 2023/5/25 15:14
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();


    public static synchronized void setDataSourceType(String dataSourceType) {
        Assert.hasText(dataSourceType, "dataSourceType cannot be empty");
        CONTEXT_HOLDER.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }


    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }

}
