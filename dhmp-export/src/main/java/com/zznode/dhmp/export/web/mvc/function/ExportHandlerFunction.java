package com.zznode.dhmp.export.web.mvc.function;

import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.ExportContextFactory;
import com.zznode.dhmp.export.ExportException;
import com.zznode.dhmp.export.ExportExecutionSupport;
import com.zznode.dhmp.export.constants.ReportType;
import com.zznode.dhmp.export.support.exporter.ExporterFactory;
import com.zznode.dhmp.export.utils.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.function.Function;


/**
 * 标准的{@link HandlerFunction}，用于实现导出功能。
 * <p>
 * 通常使用如下方式构建
 * <pre>
 * ExportHandlerFunction.builder()
 * .handle(request -> userService.list(request))
 * .exportClass(User.class)
 * .build();
 * </pre>
 * 也可使用下面更简洁的方式
 * <pre>
 * ExportHandlerFunction
 * .handle(request -> userService.list(request))
 * .export(User.class);
 * </pre>
 *
 * @author 王俊
 */
public class ExportHandlerFunction implements HandlerFunction<ServerResponse> {

    private final Function<ServerRequest, Object> executeFunction;

    private final Class<?> exportClass;

    public ExportHandlerFunction(Function<ServerRequest, Object> executeFunction, Class<?> exportClass) {
        Assert.notNull(executeFunction, "executeFunction must not be null");
        Assert.notNull(exportClass, "exportClass must not be null");
        this.executeFunction = executeFunction;
        this.exportClass = exportClass;
    }

    private ExporterFactory exportFactory;

    public void setExportFactory(ExporterFactory exportFactory) {
        this.exportFactory = exportFactory;
    }

    @Override
    public ServerResponse handle(ServerRequest request) {
        // 在获取数据之前，创建导出上下文，如果创建失败提前抛出异常
        ExportContext exportContext = createExportContext();
        // 在WriteFunction之前提前执行获取数据，如果抛出异常，将提前返回。
        Object data = executeFunction.apply(request);
        // 构建WriteFunction
        ExportWriteFunction exportWriteFunction = buildExportWriteFunction(data, exportContext);
        return ServerResponse.ok().build(exportWriteFunction);
    }

    private ExportContext createExportContext() {
        ExportContextFactory exportContextFactory = new HandlerFunctionExportContextFactory(exportClass);
        return exportContextFactory.createContext();
    }

    ExportWriteFunction buildExportWriteFunction(Object data, ExportContext exportContext) {
        ExportWriteFunction exportWriteFunction = new ExportWriteFunction(data, exportContext);
        if (this.exportFactory != null) {
            exportWriteFunction.configure(() -> exportFactory);
        }
        return exportWriteFunction;
    }

    /**
     * WriteFunction，执行导出逻辑。
     *
     * @author 王俊
     */
    private static class ExportWriteFunction extends ExportExecutionSupport implements ServerResponse.HeadersBuilder.WriteFunction {

        private final Object returnValue;
        private final ExportContext exportContext;

        public ExportWriteFunction(Object returnValue, ExportContext exportContext) {
            super();
            this.returnValue = returnValue;
            this.exportContext = exportContext;
        }


        @Nullable
        @Override
        public ModelAndView write(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
            try {
                doExport(returnValue, exportContext);
            } catch (Throwable e) {
                ResponseHelper.setRequestResponseHeader(servletResponse);
                if (e instanceof ExportException exportException) {
                    throw new CommonException(exportException.getMessage(), exportException.getCause());
                }
                ReportType type = exportContext.exportParam().getType();
                throw new CommonException("export.error", e, type.name());
            }
            return null;
        }

    }


    public static FunctionBuilder builder() {
        return new DefaultFunctionBuilder();
    }

    public static FunctionBuilder handle(Function<ServerRequest, Object> function) {
        return builder().handle(function);
    }


    /**
     * 构建器接口
     */
    public interface FunctionBuilder {

        /**
         * 处理请求，返回导出数据
         *
         * @param function 处理函数
         */
        FunctionBuilder handle(Function<ServerRequest, Object> function);

        /**
         * 配置导出类
         *
         * @param exportClass 导出类
         * @see com.zznode.dhmp.export.annotation.ReportSheet
         */
        FunctionBuilder exportClass(Class<?> exportClass);

        /**
         * 配置导出器工厂类
         *
         * @param exportFactory 导出器工厂
         */
        FunctionBuilder exportFactory(ExporterFactory exportFactory);

        /**
         * 简洁的方式，直接返回ExportHandlerFunction
         *
         * @param exportClass 导出类
         * @see #exportClass
         */
        ExportHandlerFunction export(Class<?> exportClass);

        ExportHandlerFunction build();
    }

    private static class DefaultFunctionBuilder implements FunctionBuilder {

        private Class<?> exportClass;
        private Function<ServerRequest, Object> function;

        @Nullable
        private ExporterFactory exportFactory;

        @Override
        public FunctionBuilder handle(Function<ServerRequest, Object> function) {
            this.function = function;
            return this;
        }


        @Override
        public FunctionBuilder exportClass(Class<?> exportClass) {
            this.exportClass = exportClass;
            return this;
        }


        @Override
        public FunctionBuilder exportFactory(ExporterFactory exportFactory) {
            Assert.notNull(exportFactory, "exportFactory cannot be null.");
            this.exportFactory = exportFactory;
            return this;
        }

        @Override
        public ExportHandlerFunction export(Class<?> exportClass) {
            return exportClass(exportClass).build();
        }

        @Override
        public ExportHandlerFunction build() {
            Assert.notNull(exportClass, "exportClass cannot be null.");
            Assert.notNull(function, "function cannot be null.");
            ExportHandlerFunction handlerFunction = new ExportHandlerFunction(function, exportClass);
            if (this.exportFactory != null) {
                handlerFunction.setExportFactory(exportFactory);
            }
            return handlerFunction;
        }
    }

}
