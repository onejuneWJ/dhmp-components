package com.zznode.dhmp.export.filler;

import com.zznode.dhmp.export.ExportConfig;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 数据填充器
 *
 * @author 王俊
 * @date create in 2023/7/17 16:57
 */
public interface DataFiller {

    /**
     * 填充数据
     *
     * @param data 数据
     */
    void fillData(Iterable<?> data);

    /**
     * 把字节写入输出流
     *
     * @param outputStream 输出流
     * @throws IOException IoException
     */
    void flush(OutputStream outputStream) throws IOException;

    /**
     * 配置
     *
     * @param exportConfig 配置文件
     */
    void configure(ExportConfig exportConfig);
}
