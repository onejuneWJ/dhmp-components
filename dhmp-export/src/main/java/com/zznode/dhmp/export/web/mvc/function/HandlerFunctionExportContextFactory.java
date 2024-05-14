package com.zznode.dhmp.export.web.mvc.function;

import com.zznode.dhmp.export.web.mvc.AbstractServletExportContextFactory;

/**
 * HandlerFunction中创建导出上下文
 *
 * @author 王俊
 */
public class HandlerFunctionExportContextFactory  extends AbstractServletExportContextFactory {

    private final Class<?> exportClass;

    public HandlerFunctionExportContextFactory(Class<?> exportClass) {
        this.exportClass = exportClass;
    }

    @Override
    protected Class<?> obtainExportClass() {
        return exportClass;
    }
}
