package com.zznode.dhmp.web.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.http.HttpRequest;
import java.util.Enumeration;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 请求头复制
 *
 * @author 王俊
 * @date create in 2023/8/24
 */
public class RequestHeaderCopier {

    /**
     *  webclient如果使用的jdk自带的JdkClientHttpConnector，会过滤如下的请求头，将抛出异常
     * @see HttpRequest.Builder#header(String, String)
     */
    private static final Set<String> NOT_ALLOW_HEADERS = Set.of("connection", "content-length", "expect", "host", "upgrade");

    /**
     * 复制请求头
     *
     * @param headerConsumer 请求头消费
     */
    public static void copyHeaders(BiConsumer<String, String> headerConsumer) {
        copyHeaders(headerConsumer, s -> !NOT_ALLOW_HEADERS.contains(s));
    }
    /**
     * 复制请求头
     *
     * @param headerConsumer 请求头消费
     */
    public static void copyAllHeaders(BiConsumer<String, String> headerConsumer) {
        copyHeaders(headerConsumer, ACCEPT_ALL);
    }

    /**
     * 复制请求头
     * @param headerConsumer 请求头消费
     * @param namePredicate 用于跳过指定的请求头
     */
    public static void copyHeaders(BiConsumer<String, String> headerConsumer, Predicate<String> namePredicate) {
        Assert.notNull(headerConsumer, "param headerConsumer cannot be null");
        Assert.notNull(namePredicate, "param namePredicate cannot be null");
        /*
         * 获取原线程的request对象的请求头中的token
         * RequestContextHolder.getRequestAttributes()：获取request原始的请求头对象
         * 接口类RequestAttributes不能使用，所以强转为ServletRequestAttributes类型
         */
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) {
            return;
        }
        //获取原Request对象
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //把原request的请求头的所有参数都拿出来
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            //获取每个请求头参数的名字
            String name = headerNames.nextElement();
            //获取值
            String value = request.getHeader(name);
            if (namePredicate.test(name)) {
                // 消费
                headerConsumer.accept(name, value);
            }
        }
    }

    public static Predicate<String> ACCEPT_ALL = s -> true;
}
