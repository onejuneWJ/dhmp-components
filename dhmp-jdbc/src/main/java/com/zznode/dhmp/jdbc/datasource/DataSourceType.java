package com.zznode.dhmp.jdbc.datasource;

/**
 * 数据源类型常量，建议数据源类型都用小写+破折号格式
 *
 * @author 王俊
 * @date create in 2023/5/25 16:32
 */
public interface DataSourceType {
    /**
     * 主数据源
     */
    String MASTER = "master";


    /**
     * 下面的类型为建议定义的数据源类型
     */
    String MYSQL = "mysql";
    String ORACLE = "oracle";
    String HGU_SPARK = "hgu-spark";
    String STB_SPARK = "stb-spark";
    String HBASE = "hbase";
    String CLICKHOUSE = "clickhouse";


}
