package com.zznode.dhmp.export;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 导出的上下文，参数，可以放心调用其get方法
 *
 * @author 王俊
 * @date create in 2023/7/20 16:52
 */
public interface ExportContext {
    ExportParam exportParam();
    HttpServletRequest request();
    HttpServletResponse response();
    Class<?> exportClass();
}
