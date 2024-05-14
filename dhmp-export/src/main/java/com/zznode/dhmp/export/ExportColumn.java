package com.zznode.dhmp.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zznode.dhmp.export.annotation.ReportColumn;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 导出列
 *
 * @author 王俊
 * @date create in 2023/7/17 16:28
 */
public class ExportColumn implements Cloneable, Serializable {

    @Serial
    private static final long serialVersionUID = 4200807854910297488L;

    private Long id;

    @Nullable
    private Long parentId;

    /**
     * 列标题
     */
    private String title;
    /**
     * 字段名称
     */
    private String name;

    /**
     * 排序
     */
    private int order = 0;

    /**
     * 字段类型(Integer、String。。。)
     */
    private String type;

    /**
     * 字段列表
     */
    private List<ExportColumn> children = new ArrayList<>();

    private Boolean checked;


    @JsonIgnore
    private transient ReportColumn reportColumn;

    public ExportColumn(Long id, String title, String name, @Nullable Long rootId) {
        this(id, title, name, rootId, false);
    }


    public ExportColumn(Long id, String title, String name, @Nullable Long rootId, boolean checked) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.parentId = rootId;
        this.checked = checked;
    }

    public boolean isChecked() {
        return getChecked();
    }

    @JsonIgnore
    public List<ExportColumn> getSelectedColumns() {
        return getChildren().stream().filter(ExportColumn::isChecked).collect(Collectors.toList());
    }

    @Override
    public ExportColumn clone() {
        try {
            ExportColumn clone = (ExportColumn) super.clone();
            List<ExportColumn> children = getChildren();
            List<ExportColumn> cloneChildren = new ArrayList<>();
            for (ExportColumn child : children) {
                cloneChildren.add(child.clone());
            }
            clone.setChildren(cloneChildren);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nullable
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ExportColumn> getChildren() {
        return children;
    }

    public void setChildren(List<ExportColumn> children) {
        this.children = children;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @JsonIgnore
    public ReportColumn getReportColumn() {
        return reportColumn;
    }

    public void setReportColumn(ReportColumn reportColumn) {
        this.reportColumn = reportColumn;
    }
}
