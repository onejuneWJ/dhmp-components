package com.zznode.dhmp.export.filler;

import cn.hutool.core.io.FastStringWriter;
import cn.hutool.core.text.csv.CsvWriteConfig;
import cn.hutool.core.text.csv.CsvWriter;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.dto.ExportParam;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * csv
 *
 * @author 王俊
 * @date create in 2023/7/17 17:06
 */
public class CsvDataFiller extends AbstractDataFiller {

    private final CsvWriter csvWriter;
    private final FastStringWriter fastStringWriter;

    public CsvDataFiller(ExportContext exportContext) {
        super(exportContext);
        this.fastStringWriter = new FastStringWriter();
        ExportParam exportParam = exportContext.exportParam();
        CsvWriteConfig csvWriteConfig = CsvWriteConfig.defaultConfig();
        csvWriteConfig.setFieldSeparator(exportParam.getFieldSeparator());
        this.csvWriter = new CsvWriter(fastStringWriter, csvWriteConfig);
    }

    @Override
    protected void fillTitle(List<String> titles) {
        csvWriter.writeHeaderLine(titles.toArray(new String[0]));
    }

    @Override
    protected void fillRow(List<Object> objects) {
        String[] strings = objects.stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        csvWriter.writeLine(strings);
    }

    @Override
    public void flush(OutputStream outputStream) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)){
            writer.write(this.fastStringWriter.toString());
            writer.flush();
        }
    }
}
