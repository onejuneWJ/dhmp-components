package com.zznode.dhmp.file.util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.file.constant.FileErrorCode;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class DownloadUtil {
    private static final Log logger = LogFactory.getLog(DownloadUtil.class);

    /**
     * 下载文件到响应
     * @param inputStream 输入流
     * @param response 响应
     * @param filename 文件名称
     */
    public static void download(InputStream inputStream, HttpServletResponse response, String filename) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, Charset.defaultCharset()));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IoUtil.copy(inputStream, outputStream);
        } catch (IORuntimeException ioRuntimeException) {
            response.setHeader("Content-Disposition", "");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            throw new CommonException(FileErrorCode.FILE_DOWNLOAD_ERROR, ioRuntimeException);
        } catch (IOException e) {
            // 获取响应流报的异常，不做处理，打印日志即可, 正常情况不会报这种错误，
            // 出现这个异常,说明代码有问题，如响应已经被使用或关闭之类的
            logger.error("Error downloading file", e);
        }
    }
}
