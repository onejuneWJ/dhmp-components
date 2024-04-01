package com.zznode.dhmp.export.filler;

import cn.hutool.poi.excel.ExcelWriter;
import com.zznode.dhmp.export.ExportContext;

import java.io.OutputStream;
import java.util.List;

/**
 * excel数据填充
 *
 * @author 王俊
 * @date create in 2023/7/17 16:58
 */
public final class ExcelDataFiller extends AbstractDataFiller {

    private final ExcelWriter excelWriter;

    public ExcelDataFiller(ExportContext exportContext) {
        super(exportContext);
        this.excelWriter = new ExcelWriter(true);
    }

    @Override
    protected void fillTitle(List<String> titles) {
        excelWriter.writeHeadRow(titles);
    }

    @Override
    protected void fillRow(List<Object> objects) {
        excelWriter.writeRow(objects);
    }

    @Override
    public void flush(OutputStream outputStream) {
        excelWriter.flush(outputStream);
        excelWriter.close();
    }


}
