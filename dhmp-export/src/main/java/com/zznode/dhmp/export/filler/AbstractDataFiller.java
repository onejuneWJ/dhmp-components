package com.zznode.dhmp.export.filler;

import com.zznode.dhmp.export.ExportConfig;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.dto.ExportColumn;
import com.zznode.dhmp.export.utils.ExportHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据填充抽象
 *
 * @author 王俊
 * @date create in 2023/7/20 17:28
 */
public abstract class AbstractDataFiller implements DataFiller {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<ExportColumn> columnList;
    private final ExportHelper exportHelper;
    private Integer singleExcelMaxRow;

    public AbstractDataFiller(ExportContext exportContext) {
        this.exportHelper = exportContext.exportHelper();
        this.columnList = exportHelper.getExportColumns(exportContext);
        Assert.isTrue(!columnList.isEmpty(), "columnList is empty");
    }

    public Integer getSingleExcelMaxRow() {
        return singleExcelMaxRow;
    }

    public void setSingleExcelMaxRow(Integer singleExcelMaxRow) {
        this.singleExcelMaxRow = singleExcelMaxRow;
    }

    @Override
    public void fillData(Iterable<?> data) {
        List<String> titles = columnList.stream()
                .map(ExportColumn::getTitle)
                .collect(Collectors.toList());
        fillTitle(titles);
        fillContent(data);
    }

    @Override
    public void configure(ExportConfig exportConfig) {
        this.setSingleExcelMaxRow(exportConfig.getMaxRow());
    }

    /**
     * 填充标题
     *
     * @param titles 标题行
     */
    protected abstract void fillTitle(List<String> titles);

    /**
     * 填充内容
     *
     * @param dataList 数据
     */
    protected void fillContent(Iterable<?> dataList) {
        int size = 0;
        long mapDataCost = 0;
        long fillCost = 0;
        for (Object data : dataList) {
            long start = System.nanoTime();
            List<Object> objects = columnList.stream()
                    .map(column -> exportHelper.getFieldValue(column, data))
                    .toList();
            long end = System.nanoTime();
            mapDataCost += (end - start);
            fillRow(objects);
            long fillEnd = System.nanoTime();
            fillCost += (fillEnd - end);
            size++;
            Assert.isTrue(size < getSingleExcelMaxRow(), "export.too-many-data");
        }

        logger.debug("convert data cost: " + Duration.ofNanos(mapDataCost).toMillis());
        logger.debug("fill content cost: " + Duration.ofNanos(fillCost).toMillis());
    }

    /**
     * 填充行
     *
     * @param objects 行数据列表
     */
    protected abstract void fillRow(List<Object> objects);
}
