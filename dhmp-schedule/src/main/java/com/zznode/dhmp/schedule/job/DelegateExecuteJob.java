package com.zznode.dhmp.schedule.job;

import com.zznode.dhmp.schedule.handler.AbstractJobHandler;
import com.zznode.dhmp.schedule.registry.JobHandlerRegistry;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static com.zznode.dhmp.schedule.constants.JobConstants.JOB_CODE;

/**
 * 执行的job类
 *
 * @author 王俊
 */
public final class DelegateExecuteJob extends QuartzJobBean {
    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String jobCode = jobDataMap.getString(JOB_CODE);
        AbstractJobHandler jobHandler = JobHandlerRegistry.getJobHandler(jobCode);
        jobHandler.run();

    }
}
