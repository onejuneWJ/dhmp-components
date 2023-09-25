package com.zznode.dhmp.export.exporter;

import com.zznode.dhmp.export.ExportContext;

/**
 * 导出器-工厂
 *
 * @author 王俊
 * @date create in 2023/7/18 17:59
 */
public interface ExporterFactory {

    /**
     * 创建导出器
     * @param context 导出上下文参数
     * @return 导出器
     */
    Exporter createExporter(ExportContext context);


}
