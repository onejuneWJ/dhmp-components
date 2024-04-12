package com.zznode.dhmp.export.utils;

import com.zznode.dhmp.core.constant.BaseConstants;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.constants.ReportType;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 响应头
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
public class ResponseHelper {

    private static final Log logger = LogFactory.getLog(ResponseHelper.class);

    public static void setRequestResponseHeader(HttpServletResponse response) {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "");
    }

    public static void setExportResponseHeader(ExportContext exportContext) {
        ReportType reportType = exportContext.exportParam().getType();
        String contentType = reportType.getContentType();
        String filename = getDownloadFileName(exportContext) + BaseConstants.Symbol.POINT + reportType.getFileSuffix();
        int index = filename.lastIndexOf(".");
        try {
            filename = FilenameUtils.encodeFileName(exportContext.request(), filename.substring(0, index)) + filename.substring(index);
        } catch (IOException e) {
            logger.error("encode file name failed.", e);
        }
        HttpServletResponse response = exportContext.response();
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
        response.setContentType(contentType);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
    }


    /**
     * 下载文件的名称
     *
     * @param exportContext exportContext
     */
    protected static String getDownloadFileName(ExportContext exportContext) {
        String fileName = exportContext.exportParam().getTableName();
        if (!StringUtils.hasText(fileName)) {
            ExportHelper exportHelper = exportContext.exportHelper();
            fileName = exportHelper.getSheetName(exportContext.exportClass());
        }
        return fileName;
    }
}
