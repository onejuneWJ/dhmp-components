package com.zznode.dhmp.export.exporter;

import com.zznode.dhmp.export.ExportConfig;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.filler.DataFiller;
import com.zznode.dhmp.export.utils.ResponseHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;

/**
 * 导出抽象类
 *
 * @author 王俊
 * @date create in 2023/7/18 18:00
 */
public class DefaultExporter implements Exporter {

    protected final Log logger = LogFactory.getLog(getClass());

    private final ExportContext exportContext;

    private final DataFiller dataFiller;

    protected DefaultExporter(ExportContext exportContext, DataFiller dataFiller) {
        Assert.notNull(exportContext, "exportContext cannot be null");
        Assert.notNull(dataFiller, "dataFiller cannot be null");
        this.exportContext = exportContext;
        this.dataFiller = dataFiller;
    }

    @Override
    public void export(Iterable<?> data) throws IOException {

        // 填充数据
        long start = System.nanoTime();
        fillData(data);
        long end = System.nanoTime();
        logger.debug(String.format("fill data total cost: %s ms", Duration.ofNanos(end - start).toMillis()));
        // 响应相关设置放在最后，不然报错会有问题
        ResponseHelper.setExportResponseHeader(getExportContext());
        HttpServletResponse response = exportContext.response();
        flush(response.getOutputStream());
    }

    @Override
    public void configure(ExportConfig exportConfig) {
        this.dataFiller.configure(exportConfig);
    }

    protected void flush(OutputStream outputStream) throws IOException {
        this.dataFiller.flush(outputStream);
    }

    protected void fillData(Iterable<?> data) {
        this.dataFiller.fillData(data);
    }


    protected ExportContext getExportContext() {
        return exportContext;
    }

    protected DataFiller getDataFiller() {
        return dataFiller;
    }
}
