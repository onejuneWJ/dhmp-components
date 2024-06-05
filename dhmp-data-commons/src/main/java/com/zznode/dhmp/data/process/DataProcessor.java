package com.zznode.dhmp.data.process;

import org.springframework.lang.Nullable;

/**
 * 数据处理器
 *
 * @author 王俊
 */
public interface DataProcessor {
    /**
     * 处理
     *
     * @param object 需要处理的对象
     */
    void process(@Nullable Object object);
}
