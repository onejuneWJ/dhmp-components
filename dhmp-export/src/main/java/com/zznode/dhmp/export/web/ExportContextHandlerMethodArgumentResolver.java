package com.zznode.dhmp.export.web;

import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.annotation.Export;
import com.zznode.dhmp.export.dto.ExportParam;
import com.zznode.dhmp.export.utils.ExportHelper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

/**
 * HandlerMethodArgumentResolver for {@link ExportContext}
 *
 * @author 王俊
 * @date create in 2023/9/1
 */
public class ExportContextHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final Log logger = LogFactory.getLog(getClass());

    private final ExportHelper exportHelper;

    public ExportContextHandlerMethodArgumentResolver(ObjectProvider<ExportHelper> exportHelper) {
        this.exportHelper = exportHelper.getIfAvailable(ExportHelper::new);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return ExportContext.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        ExportParam exportParam = resolveExportParamParam(webRequest, binderFactory);
        exportParam.validateParam();
        Method method = parameter.getMethod();
        if (method == null) {
            // method不可能为空
            throw new IllegalStateException("method is null.");
        }
        Export export = method.getAnnotation(Export.class);
        if (export == null) {
            logger.warn("method not annotated with com.zznode.dhmp.export.annotation.Export");
            return null;
        }
        Class<?> exportClass = export.value();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        return new ExportContext(exportParam, request, response, exportClass, exportHelper);
    }

    private ExportParam resolveExportParamParam(NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ExportParam exportParam = new ExportParam();
        WebDataBinder binder = binderFactory.createBinder(webRequest, exportParam, "exportParam");
        bindRequestParameters(binder, webRequest);
        return exportParam;
    }

    /**
     * 绑定参数
     * @param binder binder
     * @param request 请求
     */
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        if (binder instanceof ServletRequestDataBinder servletRequestDataBinder) {
            ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
            Assert.state(servletRequest != null, "No ServletRequest");
            servletRequestDataBinder.bind(servletRequest);
            return;
        }
        if (binder instanceof WebRequestDataBinder webRequestDataBinder) {
            webRequestDataBinder.bind(request);
        }

    }

}
