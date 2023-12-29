package com.zznode.dhmp.schedule.job;

import com.zznode.dhmp.schedule.handler.AbstractJobHandler;
import com.zznode.dhmp.schedule.manage.JobRecordManager;
import com.zznode.dhmp.schedule.registry.JobHandlerRegistry;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.zznode.dhmp.schedule.constants.JobConstants.JOB_CODE;

/**
 * 执行的job类,所有注册到Scheduler中的job都是这个类。通过jobCode或者实际的执行类
 *
 * @author 王俊
 */
public final class DelegateExecuteJob extends QuartzJobBean {
    private final Logger logger = LoggerFactory.getLogger(DelegateExecuteJob.class);

    private final JobRecordManager jobRecordManager;

    public DelegateExecuteJob(JobRecordManager jobRecordManager) {
        this.jobRecordManager = jobRecordManager;
    }

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String jobCode = jobDataMap.getString(JOB_CODE);
        AbstractJobHandler jobHandler = JobHandlerRegistry.getJobHandler(jobCode);
        if (jobHandler == null) {
            return;
        }
        logger.info("starting executing job with jobCode: " + jobCode);
        try {
            jobHandler.run();
            jobRecordManager.addJobRecord(jobCode, 1);
        } catch (Exception e) {
            logger.error("error occurred while executing job {}.", jobCode, e);
            jobRecordManager.addJobRecord(jobCode, 0, traceToString(e.getStackTrace()));
        }
    }

    private String traceToString(StackTraceElement[] stackTrace) {
        return Arrays.stream(stackTrace)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

}
