package com.zznode.dhmp.schedule.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

/**
 * 任务执行器抽象类
 *
 * @author 王俊
 */
public abstract class AbstractJobHandler {

    protected final Log logger = LogFactory.getLog(getClass());


    protected JobExecutionContext context;


    public void run() {
        beforeRun();
        runInternal();
        afterRun();
    }

    protected abstract void runInternal();

    protected void beforeRun() {
    }

    protected void afterRun() {

    }

    protected void onError(Exception e) {
        logger.error("error occurred while running.", e);
    }


}
