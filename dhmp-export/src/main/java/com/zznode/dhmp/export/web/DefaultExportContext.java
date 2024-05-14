package com.zznode.dhmp.export.web;

import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.ExportParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;

/**
 * 描述
 *
 * @author 王俊
 */
public record DefaultExportContext(ExportParam exportParam,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   Class<?> exportClass) implements ExportContext {
    public DefaultExportContext {
        Assert.notNull(exportParam, "ExportParam cannot be null");
        Assert.notNull(request, "Request cannot be null");
        Assert.notNull(response, "Response cannot be null");
        Assert.isTrue(exportClass != Object.class, "please specify export class.");
    }

    @Override
    public ExportParam exportParam() {
        return exportParam;
    }


    @Override
    public HttpServletRequest request() {
        return request;
    }


    @Override
    public HttpServletResponse response() {
        return response;
    }


    @Override
    public Class<?> exportClass() {
        return exportClass;
    }
}
