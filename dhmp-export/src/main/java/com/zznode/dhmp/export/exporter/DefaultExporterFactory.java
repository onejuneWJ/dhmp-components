package com.zznode.dhmp.export.exporter;

import com.zznode.dhmp.export.ExportConfig;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.constants.ReportType;
import com.zznode.dhmp.export.filler.CsvDataFiller;
import com.zznode.dhmp.export.filler.DataFiller;
import com.zznode.dhmp.export.filler.ExcelDataFiller;

/**
 * 导出器工厂默认实现
 *
 * @author 王俊
 * @date create in 2023/8/30
 */
public class DefaultExporterFactory implements ExporterFactory {

    private ExportConfig exportConfig = new ExportConfig();

    public ExportConfig getExportConfig() {
        return exportConfig;
    }

    public void setExportConfig(ExportConfig exportConfig) {
        this.exportConfig = exportConfig;
    }

    @Override
    public Exporter createExporter(ExportContext context) {
        DataFiller dataFiller = createDataFiller(context);
        DefaultExporter defaultExporter = new DefaultExporter(context, dataFiller);
        defaultExporter.configure(getExportConfig());
        return defaultExporter;
    }

    public DataFiller createDataFiller(ExportContext context) {
        ReportType reportType = context.exportParam().getType();
        return switch (reportType) {
            case CSV, TEXT -> new CsvDataFiller(context);
            case EXCEL -> new ExcelDataFiller(context);
            // 暂未实现
            case WORD, PDF, JSON, XML -> null;
        };
    }
}
