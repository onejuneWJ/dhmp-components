package com.zznode.dhmp.schedule.constants;

/**
 * 任务执行状态
 *
 * @author 王俊
 */
public enum TaskStatus {

    /**
     * 未开始
     */
    CREATED,
    /**
     * 运行中,
     */
    RUNNING,
    /**
     * 任务暂停
     */
    SUSPENDED,
    /**
     * 任务终止
     */
    STOPPED
}
