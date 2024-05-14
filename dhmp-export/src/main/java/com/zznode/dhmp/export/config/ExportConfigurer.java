package com.zznode.dhmp.export.config;

import com.zznode.dhmp.export.support.exporter.ExporterFactory;

/**
 * 自定义配置导出相关组件
 *
 * @author 王俊
 */
public interface ExportConfigurer {

   ExporterFactory getExporterFactory();
}
