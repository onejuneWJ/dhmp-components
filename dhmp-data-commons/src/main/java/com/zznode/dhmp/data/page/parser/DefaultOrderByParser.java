package com.zznode.dhmp.data.page.parser;

import org.springframework.data.domain.Sort;

import java.util.Iterator;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/7/3 17:03
 */
public class DefaultOrderByParser implements OrderByParser {

    @Override
    public String parseToOrderBy(Sort sort) {
        if (!sort.isSorted()) {
            return "";
        }
        Iterator<Sort.Order> iterator = sort.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Sort.Order order = iterator.next();
            String property = order.getProperty();
            Sort.Direction direction = order.getDirection();
            stringBuilder.append(property).append(" ").append(direction.name()).append(", ");
        }

        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        return stringBuilder.toString();
    }
}
