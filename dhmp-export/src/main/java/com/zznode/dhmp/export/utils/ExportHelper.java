package com.zznode.dhmp.export.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import com.zznode.dhmp.export.ExportConfig;
import com.zznode.dhmp.export.annotation.ReportColumn;
import com.zznode.dhmp.export.annotation.ReportColumn.EnumConvert;
import com.zznode.dhmp.export.annotation.ReportSheet;
import com.zznode.dhmp.export.annotation.ReportSheet.Suffix;
import com.zznode.dhmp.export.converter.*;
import com.zznode.dhmp.export.dto.ExportColumn;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 导出工具类
 *
 * @author wangjun
 * @date 2020/9/29
 */
public class ExportHelper {

    private static final Logger logger = LoggerFactory.getLogger(ExportHelper.class);

    /**
     * 每个类的字段缓存
     */
    private static final Map<String, ExportColumn> COLUMN_CACHE = new ConcurrentHashMap<>(4);
    /**
     * 导出配置
     */
    private ExportConfig exportConfig = new ExportConfig();

    private LovConverter lovConverter;

    public ExportConfig getExportConfig() {
        return exportConfig;
    }

    public void setExportConfig(ExportConfig exportConfig) {
        Assert.notNull(exportConfig, "ExportConfig cannot be null");
        this.exportConfig = exportConfig;
    }

    public void setLovConverter(LovConverter lovConverter) {
        this.lovConverter = lovConverter;
    }

    public String getSheetName(Class<?> exportClass) {
        ReportSheet reportSheet = AnnotationUtils.findAnnotation(exportClass, ReportSheet.class);
        if (reportSheet == null) {
            throw new IllegalStateException("No ReportSheet found");
        }
        return getSheetName(reportSheet);
    }

    /**
     * 获取excel导出文件名
     *
     * @param reportSheet reportSheet
     * @return 文件名
     */
    public String getSheetName(@Nonnull ReportSheet reportSheet) {
        String title = reportSheet.value();
        if (!StringUtils.hasText(title)) {
            title = "报表";
        }
        if (reportSheet.suffixFlag()) {
            Suffix suffix = reportSheet.suffix();
            return title + "-" + generateReportTitleSuffix(suffix);
        }
        return title;
    }

    private String generateReportTitleSuffix(Suffix suffix) {
        return switch (suffix.suffixType()) {
            case DATE -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            case UUID -> UUID.randomUUID().toString().replaceAll("-", "");
            case CUSTOM -> suffix.customSuffix();
        };
    }

    /**
     * 获取列的值,
     *
     * @param exportColumn 列对象
     * @param data         字段所属对象
     * @return 值
     */
    public Object getFieldValue(ExportColumn exportColumn, Object data) {
        String columnName = exportColumn.getName();
        Object fieldValue = getFieldValue(columnName, data);
        ReportColumn reportColumn = exportColumn.getReportColumn();
        if (reportColumn.forceReplace()) {
            return getReplaceValue(reportColumn.replaceField(), fieldValue, data);
        }
        String lovCode = reportColumn.lovCode();
        // 处理值集
        if (StringUtils.hasText(lovCode)) {
            String translate = translateLov(lovCode, fieldValue);
            if (StringUtils.hasText(translate)) {
                // 如果值集翻译失败，继续其他转换操作
                return translate;
            }
        }
        EnumConvert enumConvert = reportColumn.enumConvert();
        if (enumConvert.enumClass() != Enum.class) {
            return convertEnumValue(enumConvert, fieldValue, data);
        }
        Class<? extends Converter> converterClass = reportColumn.converter();
        if (converterClass != DefaultConverter.class) {
            Converter converter = ConverterFactory.getInstance(converterClass);
            if (converter.supports(fieldValue)) {
                Object convertValue = converter.convert(fieldValue, data);
                if (convertValue == null && !getExportConfig().getReturnNullWhenNoneReplaceField()) {
                    return fieldValue;
                }
                return convertValue;
            }
            logger.warn("value for column[{}] is not supported for converter [{}]", columnName, converter.getClass().getName());
        }
        String pattern = reportColumn.pattern();
        if (StringUtils.hasText(pattern)) {
            return format(fieldValue, pattern);
        }
        return fieldValue != null ? fieldValue : getReplaceValue(reportColumn.replaceField(), null, data);
    }

    private Object convertEnumValue(EnumConvert enumConvert, Object fieldValue, Object data) {
        EnumConverter<? extends Enum<?>> enumConverter = EnumConverterCache.getEnumConverter(enumConvert);
        return enumConverter.convert(fieldValue, data);
    }

    private String translateLov(String lovCode, Object fieldValue) {
        if (lovConverter == null) {
            logger.warn("LovConverter is null. will not translate");
            // 返回null
            return null;
        }
        return lovConverter.convert(fieldValue, lovCode);
    }


    /**
     * 格式化
     *
     * @param o       数据
     * @param pattern 格式
     * @return 格式化后的数据
     */
    private String format(Object o, String pattern) {
        if (o == null) {
            return null;
        }
        String value = o.toString();
        if (o instanceof Number) {
            value = NumberUtil.decimalFormat(pattern, o);
        } else if (o instanceof Date date) {
            value = DateUtil.format(date, pattern);
        } else if (o instanceof TemporalAccessor temporalAccessor) {
            value = DateTimeFormatter.ofPattern(pattern).format(temporalAccessor);
        }
        // 剩下的不想写了，建议使用自定义的Converter
        return value;
    }


    /**
     * 获取字段值
     *
     * @param fieldName 字段
     * @param data      字段所属对象
     * @return 字段值
     */
    public Object getFieldValue(String fieldName, Object data) {
        return ReflectUtil.getFieldValue(data, fieldName);

    }

    /**
     * 获取代替字段值
     *
     * @param replaceFieldName 代替字段的名
     * @param fieldValue       原字段值
     * @param data             字段所属对象
     * @return 代替字段值
     */
    public Object getReplaceValue(String replaceFieldName, Object fieldValue, Object data) {

        if (StringUtils.hasText(replaceFieldName) && ReflectUtil.hasField(data.getClass(), replaceFieldName)) {
            // 返回代替字段值
            return getFieldValue(replaceFieldName, data);
        }
        // 如果没有代替字段，则返回null
        // 此处需要配置,返回原字段值还是null
        return getExportConfig().getReturnNullWhenNoneReplaceField() ? null : fieldValue;

    }

    public ExportColumn getExportColumn(Class<?> exportClass) {
        String exportClassName = exportClass.getName();
        ExportColumn root = COLUMN_CACHE.get(exportClassName);
        if (root != null) {
            return root;
        }
        if (!exportClass.isAnnotationPresent(ReportSheet.class)) {
            logger.error("导出类[{}]请使用 @ReportSheet 标注", exportClassName);
            throw new IllegalStateException(String.format("导出类[%s]请使用 @ReportSheet 标注", exportClassName));
        }
        root = getExportColumnFromClass(exportClass);
        COLUMN_CACHE.putIfAbsent(exportClassName, root);
        return root;
    }

    private ExportColumn getExportColumnFromClass(Class<?> exportClass) {
        // class
        ReportSheet reportSheet = AnnotationUtils.findAnnotation(exportClass, ReportSheet.class);
        if (reportSheet == null) {
            throw new IllegalStateException("No ReportSheet found");
        }
        long rootId = 0L;
        ExportColumn root = new ExportColumn(rootId, getSheetName(reportSheet), exportClass.getSimpleName(), null);
        root.setType("Class");
        root.setChecked(true);
        // field
        Field[] fields = exportClass.getDeclaredFields();
        List<ExportColumn> children = new ArrayList<>(fields.length);
        long columnId = rootId + 1;
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ReportColumn.class)) {
                continue;
            }
            ReportColumn reportColumn = AnnotationUtils.findAnnotation(field, ReportColumn.class);
            if (reportColumn == null) {
                continue;
            }
            ExportColumn column = new ExportColumn(columnId++, reportColumn.value(), field.getName(), rootId);
            column.setOrder(reportColumn.order());
            column.setType(field.getType().getSimpleName());
            column.setChecked(reportColumn.defaultSelected());
            column.setReportColumn(reportColumn);
            children.add(column);
        }
        // 排序
        children = children.parallelStream()
                .sorted(Comparator.comparing(ExportColumn::getOrder))
                .collect(Collectors.toList());
        root.setChildren(children);
        return root;
    }

    /**
     * 获取选中的导出列
     *
     * @param exportClass 导出的实体类
     * @param checkedIds  选中的列id
     * @return root
     */
    public List<ExportColumn> getCheckedExportColumn(Class<?> exportClass, Set<Long> checkedIds) {
        ExportColumn exportColumn = getExportColumn(exportClass);
        // 避免修改到缓存中的数据, 这里新建一个实例
        ExportColumn root = exportColumn.clone();
        return checkChildren(root.getChildren(), checkedIds);

    }

    private List<ExportColumn> checkChildren(List<ExportColumn> children, Set<Long> checkedIds) {
        if (CollectionUtils.isEmpty(children)) {
            return Collections.emptyList();
        }

        return children.stream().filter(child -> checkedIds.isEmpty() || checkedIds.contains(child.getId()))
                .peek(c -> c.setChecked(true))
                .collect(Collectors.toList());
    }
}
