package com.zznode.dhmp.lov.manager;

import com.zznode.dhmp.jdbc.datasource.util.DataSourceUtil;
import com.zznode.dhmp.lov.domain.LovValue;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

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
        return DataSourceUtil.executeWithMasterDataSource(() -> jdbcClient
                .sql("select * from " + LovValue.LOV_VALUE_TABLE_NAME + " where lov_code = ?")
                .param(lovCode)
                .query(LovValue.class)
                .list());
    }

}
