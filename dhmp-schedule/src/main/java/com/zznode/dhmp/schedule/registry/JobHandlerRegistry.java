package com.zznode.dhmp.schedule.registry;

import com.zznode.dhmp.schedule.handler.AbstractJobHandler;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务执行处理器注册器
 *
 * @author 王俊
 */
public class JobHandlerRegistry {
    private static final Map<String, AbstractJobHandler> JOB_HANDLER_MAP = new ConcurrentHashMap<>();

    public static AbstractJobHandler getJobHandler(String jobCode) {
        return JOB_HANDLER_MAP.get(jobCode);
    }

    public static void registerJobHandler(@NonNull String jobCode, @NonNull AbstractJobHandler handler) {
        JOB_HANDLER_MAP.put(jobCode, handler);
    }
}
