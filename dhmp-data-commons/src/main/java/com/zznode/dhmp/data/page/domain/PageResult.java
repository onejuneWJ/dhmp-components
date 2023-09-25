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

    private Long total;
    private List<T> data;

    public PageResult(Long total, List<T> data) {
        this.total = total;
        this.data = data;
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


}
