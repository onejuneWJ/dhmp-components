package com.zznode.dhmp.export.exporter;

import com.zznode.dhmp.export.ExportConfig;

import java.io.IOException;

/**
 * 导出
 *
 * @author 王俊
 * @date create in 2023/7/17 14:39
 */
public interface Exporter {

    /**
     * 执行导出
     *
     * @param data 数据, 兼容mybatis的cursor,使用cursor+CSV导出，速度飞起.但是使用cursor+excel导出，可能会比直接把数据查完再入excel更慢
     * @throws IOException io exception
     */
    void export(Iterable<?> data) throws IOException;

    /**
     * 配置
     *
     * @param exportConfig 配置
     */
    void configure(ExportConfig exportConfig);
}
