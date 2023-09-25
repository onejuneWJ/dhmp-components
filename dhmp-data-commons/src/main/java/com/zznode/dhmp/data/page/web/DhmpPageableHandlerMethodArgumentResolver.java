package com.zznode.dhmp.data.page.web;

import com.zznode.dhmp.data.page.domain.PageRequest;
import com.zznode.dhmp.data.page.parser.OrderByParser;
import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 继承{@link PageableHandlerMethodArgumentResolver}, 让
 *
 * @author 王俊
 * @date create in 2023/7/3 14:46
 */
public class DhmpPageableHandlerMethodArgumentResolver extends PageableHandlerMethodArgumentResolver implements ApplicationContextAware {

    private OrderByParser orderByParser;
    private ApplicationContext applicationContext;

    public DhmpPageableHandlerMethodArgumentResolver(SortHandlerMethodArgumentResolver sortResolver) {
        super(sortResolver);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    @Nonnull
    public Pageable resolveArgument(@Nonnull MethodParameter methodParameter, ModelAndViewContainer mavContainer, @Nonnull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        PageRequest pageRequest = PageRequest.with(pageable);
        pageRequest.setParser(getOrderByParser());
        return pageRequest;
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public OrderByParser getOrderByParser() {
        if (this.orderByParser == null) {
            this.orderByParser = applicationContext.getBean(OrderByParser.class);
        }
        return orderByParser;
    }
}
