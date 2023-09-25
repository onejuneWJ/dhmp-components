package com.zznode.dhmp.schedule.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务执行器抽象类
 *
 * @author 王俊
 */
public abstract class AbstractJobHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public void run() {
        try {
            beforeRun();
            runInternal();
            afterRun();
        } catch (Exception e) {
            onError(e);
        }
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
