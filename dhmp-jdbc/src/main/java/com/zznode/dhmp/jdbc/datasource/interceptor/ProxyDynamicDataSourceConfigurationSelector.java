package com.zznode.dhmp.jdbc.datasource.interceptor;

import com.zznode.dhmp.jdbc.datasource.annotation.EnableDynamicDataSourceAspect;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.lang.Nullable;

/**
 * ImportSelector，用于导入配置类
 *
 * @author 王俊
 */
public class ProxyDynamicDataSourceConfigurationSelector extends AdviceModeImportSelector<EnableDynamicDataSourceAspect> {
    @Nullable
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return switch (adviceMode) {
            case PROXY -> new String[]{ProxyDynamicDataSourceConfiguration.class.getName()};
            // ASPECTJ暂时不实现
            case ASPECTJ -> new String[]{};
        };
    }
}
