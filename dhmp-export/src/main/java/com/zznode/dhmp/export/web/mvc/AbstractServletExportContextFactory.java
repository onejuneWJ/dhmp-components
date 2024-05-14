package com.zznode.dhmp.export.web.mvc;

import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.ExportContextFactory;
import com.zznode.dhmp.export.ExportParam;
import com.zznode.dhmp.export.web.DefaultExportContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * servlet环境中创建ExportContext的抽象类
 *
 * @author 王俊
 */
public abstract class AbstractServletExportContextFactory implements ExportContextFactory {

    /**
     * 创建导出上下文对象。
     * 该方法提取当前的HTTP请求和响应，解析导出参数，并基于这些信息实例化一个默认的导出上下文对象。
     *
     * @return ExportContext 返回一个包含导出操作所需全部信息的上下文对象。
     */
    @Override
    public ExportContext createContext() {
        // 获取当前的HTTP请求和响应
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        // 解析导出参数
        ExportParam exportParam = resolveExportParam(request);
        exportParam.validateParam();
        // 确保响应对象不为空
        Assert.state(response != null, "No HttpServletResponse");

        // 创建并返回一个默认的导出上下文对象
        return new DefaultExportContext(exportParam, request, response, obtainExportClass());
    }

    /**
     * 解析导出参数
     * <p>
     * 本方法旨在从request中解析出导出参数。目前的实现尚未优化处理导出参数可能存在于请求体中的情况。
     *
     * @param servletRequest 代表客户端请求的数据，用于从中提取导出参数
     * @return 解析后的ExportParam对象，包含导出所需的参数设置
     */
    protected ExportParam resolveExportParam(ServletRequest servletRequest) {
        // 创建一个用于绑定请求数据的ServletRequestDataBinder实例
        ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(null);
        // 设置目标类型为ExportParam类，以便正确解析请求参数
        dataBinder.setTargetType(ResolvableType.forClass(ExportParam.class));

        dataBinder.setRequiredFields("requestType");

        // 初始化和绑定请求数据到数据绑定器
        dataBinder.construct(servletRequest);
        dataBinder.bind(servletRequest);

        // 获取绑定结果，用于检查是否有错误
        BindingResult bindingResult = dataBinder.getBindingResult();

        // 获取绑定的目标对象，即解析后的ExportParam实例
        ExportParam result = (ExportParam) bindingResult.getTarget();
        if (result != null) {
            // 如果存在有效目标对象，则返回解析后的ExportParam对象
            return result;
        }
        else {
            // 如果绑定结果既没有目标对象也没有错误，抛出异常
            throw new IllegalStateException("Binding result has neither target nor errors");
        }
    }


    /**
     * 获取当前的Servlet请求属性。
     * 这个方法通过RequestContextHolder获取当前的请求属性，然后判断是否为ServletRequestAttributes实例。
     * 如果是，则返回该实例；如果不是，则抛出IllegalStateException异常，表明当前环境必须是Servlet环境。
     *
     * @return ServletRequestAttributes 当前的Servlet请求属性。
     * @throws IllegalStateException 如果当前环境不是Servlet环境，则抛出此异常。
     */
    protected ServletRequestAttributes getServletRequestAttributes() {
        // 从RequestContextHolder获取当前的请求属性
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 判断获取的请求属性是否为ServletRequestAttributes类型
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes;
        }
        // 如果不是Servlet环境，抛出异常
        throw new IllegalStateException("we must be in a servlet environment");
    }

    /**
     * 获取导出的类。
     * 这个方法需要子类实现，返回导出的类。
     *
     * @return 导出的类。
     */
    protected abstract Class<?> obtainExportClass();


}
