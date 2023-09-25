package com.zznode.dhmp.export.constants;

import org.springframework.http.MediaType;

/**
 * 导出文件的类型类型
 *
 * @author 王俊
 * @date create in 2023/7/17 14:01
 */
public enum ReportType {


    /**
     * excel
     */
    EXCEL("application/vnd.ms-excel", "xlsx"),
    /**
     * word
     */
    WORD(MediaType.APPLICATION_OCTET_STREAM_VALUE, "docx"),
    /**
     * csv
     */
    CSV(MediaType.APPLICATION_OCTET_STREAM_VALUE, "csv"),
    /**
     * text
     */
    TEXT(MediaType.APPLICATION_OCTET_STREAM_VALUE, "txt"),
    /**
     * pdf
     */
    PDF(MediaType.APPLICATION_OCTET_STREAM_VALUE, "pdf"),
    /**
     * json
     */
    JSON(MediaType.APPLICATION_OCTET_STREAM_VALUE, "json"),
    /**
     * xml
     */
    XML(MediaType.APPLICATION_OCTET_STREAM_VALUE, "xml");


    private final String contentType;
    private final String fileSuffix;

    ReportType(String contentType, String fileSuffix) {
        this.contentType = contentType;
        this.fileSuffix = fileSuffix;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }
}
