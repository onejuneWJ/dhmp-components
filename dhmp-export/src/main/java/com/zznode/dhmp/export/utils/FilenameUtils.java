package com.zznode.dhmp.export.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 文件名工具类
 *
 * @author 王俊
 * @date create in 2023/7/18 18:17
 */
public class FilenameUtils {

    public static String encodeFileName(HttpServletRequest request, String filename) throws IOException {
        String userAgent = request.getHeader("User-Agent");
        String encodeFilename;
        // 解决输出中文名乱码的问题
        if (!StringUtils.hasText(userAgent)) {
            encodeFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        } else if (!userAgent.contains("MSIE") && !userAgent.contains("like Gecko")) {
            encodeFilename = "=?UTF-8?B?" + Base64.getEncoder().encodeToString(filename.getBytes(StandardCharsets.UTF_8)) + "?=";
        } else {
            encodeFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        }

        return encodeFilename;
    }

}
