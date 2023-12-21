package com.zznode.dhmp.data.page.domain;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果
 *
 * @author 王俊
 * @date create in 2023/7/3 11:29
 */
public class PageResult<T> implements Serializable {

    /**
     * 总条数
     */
    private Long total;
    /**
     * 当前页数据
     */
    private List<T> rows;

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public static <T> PageResult<T> of(Long total, List<T> rows) {
        return new PageResult<>(total, rows);
    }

    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    public static <T> PageResult<T> of(List<T> data) {
        return PageResult.of(PageInfo.of(data));
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
