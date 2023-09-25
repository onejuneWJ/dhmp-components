package com.zznode.dhmp.data.page;

import com.github.pagehelper.Page;

import java.util.List;

/**
 * 查询
 *
 * @author 王俊
 * @date create in 2023/7/3 15:39
 */
@FunctionalInterface
public interface Select<E> {

    /**
     * 执行查询
     * <p>不要在该方法内写过多代码，只要一行查询方法最好
     *
     * @return 查询列表，这里返回的list就是{@link Page},此处返回类型用于约束查询
     */
    @SuppressWarnings({"UnusedReturnValue"})
    List<E> doSelect();
}
