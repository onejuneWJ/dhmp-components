package com.zznode.dhmp.jdbc.datasource.util;

import com.zznode.dhmp.jdbc.datasource.DataSourceType;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceContextHolder;
import com.zznode.dhmp.jdbc.datasource.annotation.UseDynamicDataSource;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * 数据源工具类。
 * <p>虽然有了{@link UseDynamicDataSource}注解，但是有些场景使用不太方便。
 * 如：在一个方法中可能存在多种数据源查询时，可使用下面的方法进行查询。
 *
 * @author 王俊
 * @since  2014-01-26
 */
public class DataSourceUtil {

    /**
     * 使用指定数据源执行SQL
     *
     * @param task           SQL执行
     * @param dataSourceType 数据源类型
     * @param <T>            SQL执行结果类型
     * @return SQL执行结果
     */
    public static <T> T executeWithDataSourceType(Supplier<T> task, String dataSourceType) {
        String sourceType = DynamicDataSourceContextHolder.getDataSourceType();
        DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
        try {
            // 在这里执行查询
            return task.get();
        } finally {
            // 这里要判空，如果原
            if (StringUtils.hasText(sourceType)) {
                DynamicDataSourceContextHolder.setDataSourceType(sourceType);
            }
        }
    }

    /**
     * 使用指定数据源执行SQL，无返回值
     *
     * @param task           SQL执行
     * @param dataSourceType 数据源类型
     */
    public static void executeWithDataSourceType(Runnable task, String dataSourceType) {
        executeWithDataSourceType(() -> {
            task.run();
            return null;
        }, dataSourceType);
    }

    /**
     * 使用主数据源执行SQL
     *
     * @param task SQL执行
     * @param <T>  SQL执行结果类型
     * @return SQL执行结果
     */
    public static <T> T executeWithMasterDataSource(Supplier<T> task) {
        return executeWithDataSourceType(task, DataSourceType.MASTER);
    }

}
