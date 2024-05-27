package com.zznode.dhmp.schedule;

import com.zznode.dhmp.schedule.manage.JobRecordManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.zznode.dhmp.schedule.constants.JobConstants.JOB_CODE;

/**
 * 执行的job类,所有注册到Scheduler中的job都是这个类。通过jobCode或者实际的执行类
 *
 * @author 王俊
 */
public final class DelegateExecuteJob extends QuartzJobBean implements ApplicationContextAware {
    private final Log logger = LogFactory.getLog(DelegateExecuteJob.class);

    private ApplicationContext applicationContext;

    private final JobRecordManager jobRecordManager;

    /**
     * 构造函数
     * <p>
     * 此类会在调度器每次执行任务时创建一个示例，通过{@link SpringBeanJobFactory SpringBeanJobFactory}创建的实例会在spring容器中过一遍,所以各种属性都会得到注入。
     *
     * @param jobRecordManager 任务执行记录管理器
     */
    public DelegateExecuteJob(JobRecordManager jobRecordManager) {
        this.jobRecordManager = jobRecordManager;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String jobCode = jobDataMap.getString(JOB_CODE);
        logger.debug(String.format("finding handler for jobCode [%s]", jobCode));
        AbstractJobHandler handler = getHandler(jobCode);
        if (handler == null) {
            logger.warn(String.format("no handler for jobCode [%s] to run", jobCode));
            return;
        }
        long startTime = System.currentTimeMillis();
        try {
            logger.info(String.format("starting executing job. jobCode: [%s]", jobCode));
            handler.run(context);
            jobRecordManager.addJobRecord(jobCode, 1);
        } catch (Exception e) {
            logger.error(String.format("error occurred while executing job %s.", jobCode), e);
            jobRecordManager.addJobRecord(jobCode, 0, traceToString(e.getStackTrace()));
        } finally {

            long finishTime = System.currentTimeMillis();
            logger.info(String.format("finish execute job [%s]. cost [%s] ms", jobCode, finishTime - startTime));
        }
    }

    /**
     * 获取指定任务代码的处理器。
     *
     * @param jobCode 任务的代码，用于标识不同的任务。
     * @return 如果找到对应的处理器，返回该处理器实例；如果没有找到，或者找到的不是AbstractJobHandler类型的实例，返回null。
     */
    @Nullable
    private AbstractJobHandler getHandler(String jobCode) {
        // 通过任务代码从JobHandlerRegistry获取任务处理器
        Object jobHandler = JobHandlerRegistry.getJobHandler(jobCode);
        // 如果获取到的处理器是AbstractJobHandler类型，则直接返回
        if (jobHandler instanceof AbstractJobHandler abstractJobHandler) {
            return abstractJobHandler;
        }
        // 如果获取到的处理器是String类型，则尝试从应用上下文中获取对应名称的处理器实例
        if (jobHandler instanceof String handlerName) {
            try {
                return applicationContext.getBean(handlerName, AbstractJobHandler.class);
            } catch (BeansException e) {
                logger.warn("error get bean from applicationContext. " + e.getMessage());
                return null;
            }
        }
        // 如果上述情况都不满足，则返回null
        return null;
    }


    private String traceToString(StackTraceElement[] stackTrace) {
        return Arrays.stream(stackTrace)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
