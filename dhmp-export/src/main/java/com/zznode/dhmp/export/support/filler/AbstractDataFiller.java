package com.zznode.dhmp.export.support.filler;

import com.zznode.dhmp.core.message.DhmpMessageSource;
import com.zznode.dhmp.export.ExportColumn;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.ExportException;
import com.zznode.dhmp.export.config.ExportConfigProperties;
import com.zznode.dhmp.export.utils.ExportHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    protected final Log logger = LogFactory.getLog(getClass());

    private final List<ExportColumn> columnList;
    private final ExportHelper exportHelper;
    /**
     * 每次最大导出行数
     */
    private Integer singleExcelMaxRow;
    /**
     * 是否忽略空行, 默认true
     */
    private boolean ignoreNullRows = true;

    public AbstractDataFiller(ExportContext exportContext, ExportHelper exportHelper) {
        this.exportHelper = exportHelper;
        this.columnList = exportHelper.getExportColumns(exportContext);
        Assert.isTrue(!columnList.isEmpty(), "columnList is empty");
    }

    public Integer getSingleExcelMaxRow() {
        return singleExcelMaxRow;
    }

    public void setSingleExcelMaxRow(Integer singleExcelMaxRow) {
        this.singleExcelMaxRow = singleExcelMaxRow;
    }

    public void setIgnoreNullRows(boolean ignoreNullRows) {
        this.ignoreNullRows = ignoreNullRows;
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
    public void configure(ExportConfigProperties exportConfigProperties) {
        this.setSingleExcelMaxRow(exportConfigProperties.getMaxRow());
        this.setIgnoreNullRows(exportConfigProperties.getIgnoreNullRows());
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
            ExportRow<Object> rowData;
            if (data == null) {
                rowData = new ExportRow<>();
            }
            else {
                long start = System.nanoTime();
                rowData = getRowData(data);
                long end = System.nanoTime();
                mapDataCost += (end - start);
            }

            if (rowData.isEmptyRow() && ignoreNullRows) {
                // 跳过空行
                continue;
            }

            long fillStart = System.nanoTime();
            fillRow(rowData);
            long fillEnd = System.nanoTime();
            fillCost += (fillEnd - fillStart);
            size++;
            if (size > getSingleExcelMaxRow()) {
                throw new ExportException(DhmpMessageSource.getAccessor().getMessage("export.too-many-data", new Object[]{getSingleExcelMaxRow()}));
            }
        }

        logger.debug("convert data cost: " + Duration.ofNanos(mapDataCost).toMillis() + "ms");
        logger.debug("fill content cost: " + Duration.ofNanos(fillCost).toMillis() + "ms");
    }

    /**
     * 根据提供的数据对象，获取对应的导出行数据。
     *
     * @param data 需要被导出的数据对象。
     * @return 返回一个包含导出数据的ExportRow对象，如果配置了忽略空行且生成的行为空行，则返回null。
     */
    protected ExportRow<Object> getRowData(Object data) {
        // 通过流处理，对每列数据进行处理，构造ExportRow对象
        return columnList.stream()
                .map(column -> exportHelper.getFieldValue(column, data))
                .collect(Collectors.toCollection(ExportRow::new));
    }

    /**
     * 填充行
     *
     * @param objects 行数据列表
     */
    protected abstract void fillRow(List<Object> objects);
}
