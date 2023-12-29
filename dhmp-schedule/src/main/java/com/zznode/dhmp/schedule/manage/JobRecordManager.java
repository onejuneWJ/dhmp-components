package com.zznode.dhmp.schedule.manage;

/**
 * 描述
 *
 * @author 王俊
 */
public interface JobRecordManager {

    void addJobRecord(String jobCode, Integer result);
    void addJobRecord(String jobCode, Integer result, String message);
}
