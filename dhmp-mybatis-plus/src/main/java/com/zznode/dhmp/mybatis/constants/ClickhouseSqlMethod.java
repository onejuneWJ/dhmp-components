package com.zznode.dhmp.mybatis.constants;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/6/30 18:09
 */
public enum ClickhouseSqlMethod {
    /**
     * 修改
     */
    UPDATE_BY_ID("updateByIdClickhouse", "根据ID 选择修改数据(Clickhouse)", "<script>\nALTER TABLE UPDATE %s %s WHERE %s=#{%s} %s\n</script>"),
    DELETE_BY_ID("deleteByIdClickhouse", "根据ID 删除一条数据(Clickhouse)", "<script>\nALTER TABLE %s DELETE WHERE %s=#{%s}\n</script>");
    private final String method;
    private final String desc;
    private final String sql;

    ClickhouseSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
