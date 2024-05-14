package com.zznode.dhmp.export.config;

/**
 * 导出配置
 *
 * @author 王俊
 * @date create in 2023/8/30
 */
public class ExportConfigProperties {

    private static final int DEFAULT_MAX_ROW = 100000;

    /**
     * 最大导出行数,默认10w
     */
    private Integer maxRow = DEFAULT_MAX_ROW;

    /**
     * 是否忽略空行
     */
    private boolean ignoreNullRows = true;

    public Integer getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(Integer maxRow) {
        this.maxRow = maxRow;
    }

    public boolean getIgnoreNullRows() {
        return ignoreNullRows;
    }

    public void setIgnoreNullRows(boolean ignoreNullRows) {
        this.ignoreNullRows = ignoreNullRows;
    }
}
