package com.zznode.dhmp.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

/**
 * 任务执行器抽象类
 * <p>
 * 本应该定义为接口，但是JobHandler这个名字被注解用了
 *
 * @author 王俊
 */
public abstract class AbstractJobHandler {
    protected final Log logger = LogFactory.getLog(getClass());

    public void run(JobExecutionContext context) throws Exception {
        beforeRun();
        runInternal(context);
        afterRun();
    }

    protected abstract void runInternal(JobExecutionContext context) throws Exception;

    protected void beforeRun() {

    }

    protected void afterRun() {

    }


}
