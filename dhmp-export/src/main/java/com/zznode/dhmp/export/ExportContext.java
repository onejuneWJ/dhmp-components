package com.zznode.dhmp.export;

import com.zznode.dhmp.export.dto.ExportParam;
import com.zznode.dhmp.export.utils.ExportHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;

/**
 * 导出的上下文，参数，可以放心调用其get方法
 *
 * @author 王俊
 * @date create in 2023/7/20 16:52
 */
public record ExportContext(ExportParam exportParam,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Class<?> exportClass,
                            ExportHelper exportHelper) {
    public ExportContext {
        Assert.notNull(exportParam, "ExportParam cannot be null");
        Assert.notNull(exportHelper, "ExportHelper cannot be null");
        Assert.notNull(request, "Request cannot be null");
        Assert.notNull(response, "Response cannot be null");
        Assert.isTrue(exportClass != Object.class, "please specify export class.");
    }



}
