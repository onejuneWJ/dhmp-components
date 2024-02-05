package com.zznode.dhmp.data.page.web;

import com.zznode.dhmp.data.page.domain.PageRequest;
import com.zznode.dhmp.data.page.parser.OrderByParser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

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
    public Pageable resolveArgument(MethodParameter methodParameter,
                                    @Nullable ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest,
                                    @Nullable WebDataBinderFactory binderFactory) {
        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        PageRequest pageRequest = PageRequest.with(pageable);
        pageRequest.setParser(getOrderByParser());
        getTotalParam(webRequest).ifPresent(pageRequest::setTotal);
        return pageRequest;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取前端传来的total参数
     * @param webRequest 请求
     * @return optional。存在则返回。不存在或者total参数不是数字类型返回Optional.empty()
     */
    protected Optional<Long> getTotalParam(NativeWebRequest webRequest){
        String total = webRequest.getParameter("total");
        if(StringUtils.hasText(total)){
            try {
               return Optional.of(Long.parseLong(total));
            } catch (NumberFormatException ignored) {
                // 不处理，可能查询参数中的其他参数跟total名称重复了。
            }
        }
        return Optional.empty();
    }

    protected OrderByParser getOrderByParser() {
        if (this.orderByParser == null) {
            this.orderByParser = applicationContext.getBean(OrderByParser.class);
        }
        return orderByParser;
    }
}
