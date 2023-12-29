package com.zznode.dhmp.schedule.annotation;

import com.zznode.dhmp.schedule.constants.TaskStatus;
import com.zznode.dhmp.schedule.handler.AbstractJobHandler;
import com.zznode.dhmp.schedule.job.DelegateExecuteJob;
import com.zznode.dhmp.schedule.registry.JobHandlerRegistry;
import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.lang.NonNull;

import java.util.Map;

import static com.zznode.dhmp.schedule.constants.JobConstants.JOB_CODE;

/**
 * 初始化类
 *
 * @author 王俊
 */
public class ScheduleInitializingBean implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    private final JdbcClient jdbcClient;
    private final Scheduler scheduler;

    public ScheduleInitializingBean(JdbcClient jdbcClient, Scheduler scheduler) {
        this.jdbcClient = jdbcClient;
        this.scheduler = scheduler;
    }

    @Override
    public void afterPropertiesSet() {
        registerAllJobHandlers();
        scheduleJobs();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 扫描并注册执行器
     */
    private void registerAllJobHandlers() {
        Map<String, Object> beansWithAnnotation = this.applicationContext.getBeansWithAnnotation(JobHandler.class);
        for (Object value : beansWithAnnotation.values()) {
            JobHandler annotation = AnnotationUtils.findAnnotation(value.getClass(), JobHandler.class);
            if (annotation == null) {
                continue;
            }
            String jobCode = annotation.code();
            if (value instanceof AbstractJobHandler jobHandler) {
                JobHandlerRegistry.registerJobHandler(jobCode, jobHandler);
            }
        }
    }

    /**
     * 获取需要执行的任务，注册到scheduler中。
     * <p>一般情况下，数据库中保存的任务，在创建时已经注册过了。
     * 这个方法是避免有些定时任务手动在数据库中创建，得不到执行。
     */
    private void scheduleJobs() {
        jdbcClient.sql("SELECT * FROM scheduler_task where `status` = '" + TaskStatus.RUNNING + "' and delete_flag = 0")
                .query()
                .listOfRows()
                .forEach(map -> {
                    JobDetail jobDetail = JobBuilder.newJob(DelegateExecuteJob.class)
                            .withIdentity(String.valueOf(map.get("job_name")), "default")
                            .requestRecovery(false)
                            .build();
                    Trigger trigger = TriggerBuilder.newTrigger()
                            .forJob(jobDetail)
                            .withDescription(String.valueOf(map.get("description")))
                            .withSchedule(CronScheduleBuilder.cronSchedule(String.valueOf(map.get("cron"))))
                            .usingJobData(JOB_CODE, String.valueOf(map.get("job_code")))
                            .build();
                    try {
                        if (!scheduler.checkExists(jobDetail.getKey())) {
                            scheduler.scheduleJob(jobDetail, trigger);
                        }
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }
                });


    }
}
