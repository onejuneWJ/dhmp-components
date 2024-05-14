package com.zznode.dhmp.export.web.reactive;

import com.zznode.dhmp.export.ExportContext;
import com.zznode.dhmp.export.ExportParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;

/**
 * 描述
 *
 * @author 王俊
 */
public class ReactiveExportContext implements ExportContext {

    private final ExportParam exportParam;
    private final ServerWebExchange exchange;

    public ReactiveExportContext(ExportParam exportParam, ServerWebExchange exchange) {
        this.exportParam = exportParam;
        this.exchange = exchange;
    }


    @Override
    public ExportParam exportParam() {
        return exportParam;
    }


    @Override
    public HttpServletRequest request() {
        return ServerHttpRequestDecorator.getNativeRequest(exchange.getRequest());
    }


    @Override
    public HttpServletResponse response() {
        return ServerHttpResponseDecorator.getNativeResponse(exchange.getResponse());
    }


    @Override
    public Class<?> exportClass() {
        return null;
    }
}
