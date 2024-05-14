package com.zznode.dhmp.export.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zznode.dhmp.export.ExportColumn;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.ExportParam;
import com.zznode.dhmp.export.annotation.ReportColumn;
import com.zznode.dhmp.export.annotation.ReportColumn.EnumConvert;
import com.zznode.dhmp.export.annotation.ReportSheet;
import com.zznode.dhmp.export.annotation.ReportSheet.Suffix;
import com.zznode.dhmp.export.config.ExportConfigProperties;
import com.zznode.dhmp.export.converter.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
import java.util.stream.StreamSupport;

/**
 * 导出工具类
 *
 * @author wangjun
 * @date 2020/9/29
 */
public class ExportHelper {

    private static final Log logger = LogFactory.getLog(ExportHelper.class);

    /**
     * 每个类的字段缓存
     */
    private static final Map<Class<?>, ExportColumn> COLUMN_CACHE = new ConcurrentHashMap<>(4);
    /**
     * 缓存每个类的字段,避免上万条数据每次都反射获取字段,空间换时间
     */
    private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>(4);
    /**
     * 导出配置
     */
    private ExportConfigProperties exportConfigProperties = new ExportConfigProperties();

    private LovConverter lovConverter;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ExportConfigProperties getExportConfig() {
        return exportConfigProperties;
    }

    public void setExportConfig(ExportConfigProperties exportConfigProperties) {
        Assert.notNull(exportConfigProperties, "ExportConfig cannot be null");
        this.exportConfigProperties = exportConfigProperties;
    }

    public void setLovConverter(LovConverter lovConverter) {
        this.lovConverter = lovConverter;
    }

    @Nullable
    public LovConverter getLovConverter() {
        return lovConverter;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "objectMapper cannot be null");
        this.objectMapper = objectMapper;
    }

    public static String getSheetName(Class<?> exportClass) {
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
    public static String getSheetName(@NonNull ReportSheet reportSheet) {
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

    private static String generateReportTitleSuffix(Suffix suffix) {
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
    @Nullable
    public Object getFieldValue(ExportColumn exportColumn, @Nullable Object data) {
        if (data == null) {
            return null;
        }
        String columnName = exportColumn.getName();
        Object fieldValue = getFieldValue(columnName, data);
        ReportColumn reportColumn = exportColumn.getReportColumn();
        if (reportColumn.forceReplace()) {
            return getReplaceValue(reportColumn.replaceField(), fieldValue, data);
        }
        if (fieldValue == null) {
            // 如果该字段是null,尝试获取代替字段
            fieldValue = getReplaceValue(reportColumn.replaceField(), null, data);
        }
        if (fieldValue == null) {
            // 如果还是null,就返回null吧
            return null;
        }
        String lovCode = reportColumn.lovCode();
        // 处理值集
        if (StringUtils.hasText(lovCode)) {
            String translate = translateLov(lovCode, fieldValue);
            if (StringUtils.hasText(translate)) {
                return translate;
            }
            // 如果值集翻译失败，继续其他转换操作
        }
        // 枚举转换
        EnumConvert enumConvert = reportColumn.enumConvert();
        if (enumConvert.enumClass() != Enum.class) {
            return convertEnumValue(enumConvert, fieldValue, data);
        }
        Class<? extends Converter> converterClass = reportColumn.converter();
        if (converterClass != DefaultConverter.class) {
            Converter converter = ConverterFactory.getInstance(converterClass);
            if (converter.supports(fieldValue)) {
                return converter.convert(fieldValue, data);
            }
            logger.warn(String.format("value for column [%s] is not supported for converter [%s]. ", columnName, converter.getClass().getName()));
        }
        String pattern = reportColumn.pattern();
        if (StringUtils.hasText(pattern)) {
            return format(fieldValue, pattern);
        }
        return fieldValue;
    }

    private Object convertEnumValue(EnumConvert enumConvert, Object fieldValue, Object data) {
        EnumConverter<? extends Enum<?>> enumConverter = EnumConverterCache.getEnumConverter(enumConvert);
        return enumConverter.convert(fieldValue, data);
    }

    @Nullable
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
    @Nullable
    private String format(@Nullable Object o, String pattern) {
        if (o == null) {
            return null;
        }
        String value = o.toString();
        if (o instanceof Number) {
            value = NumberUtil.decimalFormat(pattern, o);
        }
        else if (o instanceof Date date) {
            value = DateUtil.format(date, pattern);
        }
        else if (o instanceof TemporalAccessor temporalAccessor) {
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
    @Nullable
    public Object getFieldValue(String fieldName, Object data) {
        // 支持List<Map<String, Object>>数据导出
        if (data instanceof Map<?, ?> map) {
            return map.get(fieldName);
        }
        Map<String, Field> fieldMap = FIELD_CACHE.get(data.getClass());
        if (fieldMap != null) {
            // 正常情况，不会为空
            return ReflectUtil.getFieldValue(data, fieldMap.get(fieldName));
        }
        // 不排除某些玄学会执行到这里
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
    @Nullable
    public Object getReplaceValue(String replaceFieldName, @Nullable Object fieldValue, Object data) {
        if (StringUtils.hasText(replaceFieldName) && hasField(data, replaceFieldName)) {
            // 返回代替字段值
            return getFieldValue(replaceFieldName, data);
        }
        // 如果没有代替字段，则返回原字段值
        logger.warn("no replace field supplied, return original field value");
        return fieldValue;
    }

    /**
     * 判断对象中是否包含fieldName字段
     *
     * @param object    对象
     * @param fieldName 字段名称
     * @return 如果对象是map返回map包含fieldName的key, 否则返回对象中是否存在fieldName字段
     */
    private boolean hasField(Object object, String fieldName) {
        if (object instanceof Map<?, ?> map) {
            return map.containsKey(fieldName);
        }

        Map<String, Field> fieldMap = FIELD_CACHE.get(object.getClass());
        return fieldMap != null && fieldMap.containsKey(fieldName);
    }

    public ExportColumn getExportColumn(Class<?> exportClass) {
        String exportClassName = exportClass.getName();
        return COLUMN_CACHE.computeIfAbsent(exportClass, (key) -> {
                    if (!exportClass.isAnnotationPresent(ReportSheet.class)) {
                        logger.error(String.format("导出类[%s]请使用 @ReportSheet 标注", exportClassName));
                        throw new IllegalStateException(String.format("导出类[%s]请使用 @ReportSheet 标注", exportClassName));
                    }
                    return getExportColumnFromClass(exportClass);
                })
                // 避免修改到缓存中的数据, 每次返回一个新的实例
                .clone();
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
        Map<String, Field> fieldMap = FIELD_CACHE.computeIfAbsent(exportClass, (k) -> ReflectUtil.getFieldMap(exportClass));
        List<ExportColumn> children = new ArrayList<>(fieldMap.size());
        long columnId = rootId + 1;
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            String fieldName = entry.getKey();
            Field field = entry.getValue();
            if (!field.isAnnotationPresent(ReportColumn.class)) {
                continue;
            }
            ReportColumn reportColumn = AnnotationUtils.findAnnotation(field, ReportColumn.class);
            if (reportColumn == null) {
                continue;
            }
            ExportColumn column = new ExportColumn(columnId++, reportColumn.value(), fieldName, rootId);
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

    public List<ExportColumn> getExportColumns(ExportContext exportContext) {
        ExportParam exportParam = exportContext.exportParam();
        if (CollectionUtils.isEmpty(exportParam.getIds()) && StringUtils.hasText(exportParam.getColumns())) {
            // 如果使用的是旧版导出参数
            ExportColumn root = getExportColumn(exportContext.exportClass());
            try {
                JsonNode jsonNode = this.objectMapper.readTree(exportParam.getColumns());
                JsonNode column = jsonNode.get("column");
                // 前端传入的导出字段列表
                Map<String, String> exportFields = StreamSupport.stream(column.spliterator(), false)
                        .collect(Collectors.toUnmodifiableMap(
                                node -> node.get("field").asText(),
                                node -> node.get("title").asText()
                        ));
                return root.getChildren()
                        .stream()
                        .filter(child -> exportFields.containsKey(child.getName()))
                        .peek(c -> c.setTitle(exportFields.get(c.getName())))
                        .peek(c -> c.setChecked(true))
                        .toList();
            } catch (JsonProcessingException e) {
//                throw new RuntimeException("error parse columns.", e);
                // 最新版应该传参,分割。field1,field2,field3
                List<String> list = Arrays.stream(exportParam.getColumns().split(",")).toList();
                return root.getChildren()
                        .stream()
                        .filter(child -> list.contains(child.getName()))
                        .peek(c -> c.setChecked(true))
                        .toList();
            }


        } else {
            return getCheckedExportColumn(exportContext.exportClass(), exportParam.getIds());
        }
    }

    /**
     * 获取选中的导出列
     *
     * @param exportClass 导出的实体类
     * @param checkedIds  选中的列id
     * @return root
     */
    public List<ExportColumn> getCheckedExportColumn(Class<?> exportClass, Set<Long> checkedIds) {
        ExportColumn root = getExportColumn(exportClass);
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
