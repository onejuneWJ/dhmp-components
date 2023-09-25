package com.zznode.dhmp.export;

/**
 * 导出配置
 *
 * @author 王俊
 * @date create in 2023/8/30
 */
public class ExportConfig {

    private static final int DEFAULT_MAX_ROW = 100000;

    /**
     * 最大导出行数,默认10w
     */
    private Integer maxRow = DEFAULT_MAX_ROW;

    /**
     * 获取代替字段值时，如果没有设置代替字段，是否返回null值
     */
    private Boolean returnNullWhenNoneReplaceField = true;

    public Integer getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(Integer maxRow) {
        this.maxRow = maxRow;
    }

    public Boolean getReturnNullWhenNoneReplaceField() {
        return returnNullWhenNoneReplaceField;
    }

    public void setReturnNullWhenNoneReplaceField(Boolean returnNullWhenNoneReplaceField) {
        this.returnNullWhenNoneReplaceField = returnNullWhenNoneReplaceField;
    }
}
