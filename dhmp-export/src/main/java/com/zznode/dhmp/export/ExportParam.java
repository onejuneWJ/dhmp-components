package com.zznode.dhmp.export;

import com.zznode.dhmp.export.constants.ReportType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;

/**
 * 导出参数
 *
 * @author 王俊
 * @date create in 2023/7/17 14:48
 */

public class ExportParam {

    /**
     * 报表类型
     */
    private ReportType type;

    /**
     * 导出报表名称
     */
    private String tableName;

    private Set<Long> ids;

    private Integer singleSheetMaxRow;

    /**
     * 异步导出,后续实现
     */
    private Boolean async = false;

    /**
     * 以下字段兼容bootstrapTableExport
     */
    private String columns;

    /**
     * 导出csv时的字段分隔符
     */
    private char fieldSeparator = ',';


    public void validateParam() {
        Assert.notNull(type, "type must not be null");
        Assert.isTrue(Objects.nonNull(ids) || StringUtils.hasText(columns), "export columns must not be null");
    }


    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    public Integer getSingleSheetMaxRow() {
        return singleSheetMaxRow;
    }

    public void setSingleSheetMaxRow(Integer singleSheetMaxRow) {
        this.singleSheetMaxRow = singleSheetMaxRow;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public char getFieldSeparator() {
        return fieldSeparator;
    }

    public void setFieldSeparator(char fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
    }
}
