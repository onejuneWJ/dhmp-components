package com.zznode.dhmp.export.support.exporter;

import com.zznode.dhmp.export.config.ExportConfigProperties;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.constants.ReportType;
import com.zznode.dhmp.export.support.filler.CsvDataFiller;
import com.zznode.dhmp.export.support.filler.DataFiller;
import com.zznode.dhmp.export.support.filler.ExcelDataFiller;
import com.zznode.dhmp.export.utils.ExportHelper;
import org.springframework.util.Assert;

/**
 * 导出器工厂默认实现
 *
 * @author 王俊
 * @date create in 2023/8/30
 */
public class DefaultExporterFactory implements ExporterFactory {

    private ExportConfigProperties exportConfigProperties = new ExportConfigProperties();

    private ExportHelper exportHelper = new ExportHelper();

    public ExportConfigProperties getExportConfig() {
        return exportConfigProperties;
    }

    public void setExportConfig(ExportConfigProperties exportConfigProperties) {
        this.exportConfigProperties = exportConfigProperties;
    }

    private ExportHelper getExportHelper() {
        return exportHelper;
    }

    public void setExportHelper(ExportHelper exportHelper) {
        Assert.notNull(exportHelper, "ExportHelper cannot be null");
        this.exportHelper = exportHelper;
    }

    @Override
    public Exporter createExporter(ExportContext context) {
        DataFiller dataFiller = createDataFiller(context);
        return new DefaultExporter(context, dataFiller);
    }

    public DataFiller createDataFiller(ExportContext context) {
        ReportType reportType = context.exportParam().getType();
        DataFiller dataFiller = switch (reportType) {
            case CSV, TEXT -> new CsvDataFiller(context, getExportHelper());
            case EXCEL -> new ExcelDataFiller(context, getExportHelper());
            // 暂未实现
            case WORD, PDF, JSON, XML -> throw new IllegalArgumentException("unsupported export type");
        };
        // 配置
        dataFiller.configure(exportConfigProperties);
        return dataFiller;
    }
}
