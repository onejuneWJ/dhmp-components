package com.zznode.dhmp.jdbc.datasource.interceptor;

import com.zznode.dhmp.jdbc.datasource.DataSourceType;
import com.zznode.dhmp.jdbc.datasource.DynamicDataSourceContextHolder;
import com.zznode.dhmp.jdbc.datasource.annotation.UseDynamicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * 动态数据源执行支持
 *
 * @author 王俊
 */
public abstract class DynamicDataSourceAspectSupport {


    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 在指定的数据源上下文中调用方法。
     * 该方法会根据当前的上下文设置数据源类型，并在方法执行前后恢复原有的数据源类型。
     * 如果执行过程中改变了数据源上下文，将会在方法返回前恢复到原始状态。
     *
     * @param method      要调用的方法
     * @param targetClass 目标类，方法所属的类，可以为null
     * @param invocation  调用的回调对象，负责执行实际的方法调用
     * @return 调用方法后的返回值
     * @throws Throwable 如果方法执行过程中发生异常，则抛出
     */
    @Nullable
    protected Object invokeWithinDynamicDataSource(Method method, @Nullable Class<?> targetClass,
                                                   final InvocationCallback invocation) throws Throwable {
        // 获取当前线程绑定的数据源类型
        String preDataSourceType = DynamicDataSourceContextHolder.getDataSourceType();
        // 根据方法和目标类确定当前需要使用的数据源类型
        String dataSourceType = determineDataSourceType(method, targetClass);
        DynamicDataSourceContextHolder.setDataSourceType(dataSourceType);
        logger.info("prev datasource type: '" + preDataSourceType + "', use datasource type: '" + dataSourceType + "'");
        Object retVal;
        try {
            // 在设置好的数据源上下文中执行回调中的方法
            retVal = invocation.proceedWithInvocation();
        } finally {
            // 无论方法执行结果如何，最后都要恢复原有的数据源类型或清除设置
            if (StringUtils.hasText(preDataSourceType)) {
                DynamicDataSourceContextHolder.setDataSourceType(preDataSourceType);
            }
            else {
                DynamicDataSourceContextHolder.clearDataSourceType();
            }
            String nowType = DynamicDataSourceContextHolder.getDataSourceType();
            logger.info("restore datasource type to '" + nowType + "'");
        }
        return retVal;
    }


    /**
     * 确定数据源类型。
     * 本方法用于根据给定的方法和目标类确定应该使用哪种类型的数据源。
     * 首先尝试从方法本身及其声明类上解析数据源类型；如果未找到，
     * 则默认使用{@link DataSourceType#MASTER}作为数据源类型。
     *
     * @param method 要确定数据源类型的方法。
     * @param targetClass 方法所在的目标类，可以为null。如果为null，则方法所在类将用于后续处理。
     * @return 数据源类型的字符串表示。返回值可能为null，此时默认使用{@link DataSourceType#MASTER}。
     */
    protected String determineDataSourceType(Method method, @Nullable Class<?> targetClass) {

        // 获取最具体的方法，考虑AOP代理的情况
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        // 尝试从方法本身解析数据源类型
        String dataSourceType = parseDataSourceType(specificMethod);
        if (StringUtils.hasText(dataSourceType)) {
            return dataSourceType;
        }
        // 如果方法上未定义，则尝试从方法的声明类上解析数据源类型
        dataSourceType= parseDataSourceType(specificMethod.getDeclaringClass());
        if (StringUtils.hasText(dataSourceType)) {
            return dataSourceType;
        }
        // 如果方法和类上都未定义，则默认使用MASTER数据源类型
        return DataSourceType.MASTER;
    }


    /**
     * 解析数据源类型。
     * 该方法用于从注解元素中解析使用动态数据源的类型。它尝试从给定的注解元素中获取{@link UseDynamicDataSource @UseDynamicDataSource}注解的属性，
     * 并从中提取数据源类型。如果找到了{@link UseDynamicDataSource @UseDynamicDataSource}注解并指定了dataSourceType，就返回该类型字符串；
     * 如果未找到或未指定，则返回null。
     *
     * @param annotatedElement 要解析的注解元素，通常是一个类或方法。
     * @return 数据源类型的字符串表示，如果未指定则返回null。
     */
    @Nullable
    private String parseDataSourceType(AnnotatedElement annotatedElement) {
        // 尝试从注解元素中获取UseDynamicDataSource注解的属性
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                annotatedElement, UseDynamicDataSource.class, false, false);

        // 如果找到了UseDynamicDataSource注解，返回数据源类型字符串；否则返回null
        return attributes != null ? attributes.getString("dataSourceType") : null;
    }

    @FunctionalInterface
    protected interface InvocationCallback {

        @Nullable
        Object proceedWithInvocation() throws Throwable;
    }
}
