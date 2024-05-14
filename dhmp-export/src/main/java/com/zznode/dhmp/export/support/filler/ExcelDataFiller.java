package com.zznode.dhmp.export.support.filler;

import cn.hutool.poi.excel.ExcelWriter;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.utils.ExportHelper;

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

    public ExcelDataFiller(ExportContext exportContext, ExportHelper exportHelper) {
        super(exportContext, exportHelper);
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
