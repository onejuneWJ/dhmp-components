package com.zznode.dhmp.schedule;

import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务执行处理器注册器
 *
 * @author 王俊
 */
public class JobHandlerRegistry {
    /**
     * 之前的逻辑，map里保存的是JobHandler的实例，现在改成默认保存实例的bean名称，通过beanName从spring容器中获取，更节省空间
     */
    private static final Map<String, Object> JOB_HANDLER_MAP = new ConcurrentHashMap<>(16);
    private static final Map<String, String> REGISTERED_BEAN_NAMES = new ConcurrentHashMap<>(16);

    @Nullable
    public static Object getJobHandler(String jobCode) {
        return JOB_HANDLER_MAP.get(jobCode);
    }

    public static void registerJobHandler(String jobCode, Object handler) {
        JOB_HANDLER_MAP.put(jobCode, handler);
        if (handler instanceof String beanName) {
            REGISTERED_BEAN_NAMES.put(beanName, jobCode);
        }
    }

    public static void removeJobHandler(String jobCode) {
        Object removed = JOB_HANDLER_MAP.remove(jobCode);
        if (removed instanceof String beanName) {
            REGISTERED_BEAN_NAMES.remove(beanName);
        }
    }

    public static void removeJobHandlerBeanName(String beanName) {
        if (REGISTERED_BEAN_NAMES.containsKey(beanName)) {
            JOB_HANDLER_MAP.remove(REGISTERED_BEAN_NAMES.get(beanName));
            REGISTERED_BEAN_NAMES.remove(beanName);
        }
    }
}
