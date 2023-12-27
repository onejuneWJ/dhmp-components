package com.zznode.dhmp.lov.manager;

import com.zznode.dhmp.jdbc.datasource.DataSourceType;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceContextHolder;
import com.zznode.dhmp.lov.domain.LovValue;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 基于jdbc的LOV管理
 *
 * @author 王俊
 */
public class JdbcLovManager implements LovManager {

    private final JdbcClient jdbcClient;

    public JdbcLovManager(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<LovValue> getLovValues(String lovCode) {
        return doWithMasterDataSource(() -> jdbcClient
                .sql("select * from " + LovValue.LOV_VALUE_TABLE_NAME + " where lov_code = ?")
                .param(lovCode)
                .query(LovValue.class)
                .list());
    }


    /**
     * 查询使用主数据源
     * @param callable 查询操作
     * @return 返回查询结果
     * @param <T> 泛型
     */
    private <T> T doWithMasterDataSource(Callable<T> callable) {
        String sourceType = DynamicDataSourceContextHolder.getDataSourceType();
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DynamicDataSourceContextHolder.setDataSourceType(sourceType);
        }
    }
}
