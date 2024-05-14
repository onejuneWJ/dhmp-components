package com.zznode.dhmp.export;

/**
 * {@link ExportContext} 的工厂
 *
 * @author 王俊
 */
public interface ExportContextFactory {

    /**
     * 创建导出上下文
     * @return  ExportContext
     */
    ExportContext createContext();
}
