package com.zznode.dhmp.jdbc.datasource;

/**
 * 数据源类型常量
 *
 * @author 王俊
 * @date create in 2023/5/25 16:32
 */
public interface DataSourceType {
    /**
     *
     */
    String MASTER = "MASTER";
    String HGU_SPARK = "HGU_SPARK";
    String STB_SPARK = "STB_SPARK";
    String HBASE = "HBASE";
    String CLICKHOUSE = "CLICKHOUSE";


}
