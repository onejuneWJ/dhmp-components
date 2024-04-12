package com.zznode.dhmp.export;

import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.core.message.DhmpMessageSource;
import com.zznode.dhmp.export.annotation.Export;
import com.zznode.dhmp.export.constants.ReportType;
import com.zznode.dhmp.export.dto.ExportColumn;
import com.zznode.dhmp.export.dto.ExportParam;
import com.zznode.dhmp.export.exporter.DefaultExporterFactory;
import com.zznode.dhmp.export.exporter.ExporterFactory;
import com.zznode.dhmp.export.utils.ExportHelper;
import com.zznode.dhmp.export.utils.ResponseHelper;
import com.zznode.dhmp.export.utils.ResponseWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * 数据导出切面
 *
 * @author 王俊
 * @date create in 2023/7/17 14:03
 */
@Aspect
public final class ExportAspect implements MessageSourceAware, InitializingBean {

    private final Log logger = LogFactory.getLog(ExportAspect.class);


    private MessageSourceAccessor messages = DhmpMessageSource.getAccessor();

    private ExportHelper exportHelper = new ExportHelper();

    private ExporterFactory exporterFactory = new DefaultExporterFactory();

    private ExportHelper getExportHelper() {
        return exportHelper;
    }

    public void setExportHelper(ExportHelper exportHelper) {
        Assert.notNull(exportHelper, "ExportHelper cannot be null");
        this.exportHelper = exportHelper;
    }

    private ExporterFactory getExportFactory() {
        return exporterFactory;
    }

    public void setExportFactory(ExporterFactory exporterFactory) {
        Assert.notNull(exporterFactory, "ExportFactory cannot be null");
        this.exporterFactory = exporterFactory;
    }

    @Around(value = "@annotation(export)")
    public Object processExport(ProceedingJoinPoint joinPoint, Export export) throws Throwable {

        ExportContext exportContext = null;
        ExportParam exportParam;
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof ExportContext) {
                exportContext = (ExportContext) arg;
                break;
            }
        }
        if (exportContext == null) {

            throw new CommonException(messages.getMessage("export.none-export-context-provide"));
        }
        exportParam = exportContext.exportParam();
        HttpServletResponse response = exportContext.response();
        if (exportParam.isColumnRequest()) {
            ExportColumn exportColumn = getExportHelper().getExportColumn(export.value());

            ResponseWriter.write(response, exportColumn);
        } else {
            try {
                doExport(exportContext, joinPoint);
            } catch (Throwable e) {
                ResponseHelper.setRequestResponseHeader(response);
                ReportType type = exportContext.exportParam().getType();
                throw new CommonException(messages.getMessage("export.error", new Object[]{type.name()}), e);
            }

        }
        return null;
    }

    private void doExport(ExportContext exportContext, ProceedingJoinPoint joinPoint) throws Throwable {

        long getDataStart = System.nanoTime();
        Iterable<?> data = exeForExportData(joinPoint.proceed());
        long getDataEnd = System.nanoTime();
        logger.debug(String.format("fetch data cost %s ms", Duration.ofNanos(getDataEnd - getDataStart).toMillis()));
        getExportFactory()
                .createExporter(exportContext)
                .export(data);
        long exportEnd = System.nanoTime();
        logger.debug(String.format("export data cost %s ms", Duration.ofNanos(exportEnd - getDataEnd).toMillis()));
    }

    /**
     * 获取导出的数据
     *
     * @param result 方法返回值
     * @return 实际数据
     */
    private Iterable<?> exeForExportData(Object result) {

        if (result instanceof Iterable<?> iterable) {
            return iterable;
        } else if (result instanceof ResponseEntity<?> responseEntity) {
            if (responseEntity.getBody() instanceof Iterable<?> iterable) {
                return iterable;
            }
        }
        throw new IllegalArgumentException("response data type must be [? extends java.lang.Iterable] or ResponseEntity<? extends java.lang.Iterable>");
    }


    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void afterPropertiesSet() {
        DhmpMessageSource.addBasename("messages.messages-export");
    }
}
