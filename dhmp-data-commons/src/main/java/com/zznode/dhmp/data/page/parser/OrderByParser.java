package com.zznode.dhmp.data.page.parser;

import org.springframework.data.domain.Sort;

/**
 * OrderBy解析器
 *
 * @author 王俊
 * @date create in 2023/7/3 16:57
 */
public interface OrderByParser {

    /**
     * 解析sort对象为order by字符串
     *
     * @param sort sort对象
     * @return order by
     */
    String parseToOrderBy(Sort sort);
}
