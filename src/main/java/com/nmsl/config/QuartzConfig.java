package com.nmsl.config;

import com.nmsl.common.DatabaseBakTimingTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Objects;

/**
 * @author paracosm
 * @version 1.0
 * @date 2021/4/27 22:28
 */
@Configuration
@Slf4j
public class QuartzConfig {

    /**
     * 初始化的cron表达式(每周五15点10分触发)
     */
    private static final String TASK_TIME = "0 15 10 ? * FRI";

    /**
     * 方法名：
     * 功能：配置定时任务
     * 描述：
     * 创建人：paracosm
     * 创建时间：2021/4/27 23:13
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    @Bean(name = "jobDetail")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(DatabaseBakTimingTask task){
        // ScheduleTask为需要执行的任务
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        /*
         * 是否并发执行
         * 例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
         * 如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
         */
        jobDetail.setConcurrent(true);
        //设置定时任务的名字
        jobDetail.setName("scheduler");
        //设置任务的分组，这些属性都可以在数据库中，在多任务的时候使用
        jobDetail.setGroup("scheduler_group");
        //为需要执行的实体类对应的对象
        jobDetail.setTargetObject(task);
        /*
         * start为需要执行的方法
         * 通过这几个配置，告诉JobDetailFactoryBean我们需要执行定时执行ScheduleTask类中的start方法
         */
        jobDetail.setTargetMethod("start");
        log.info("jobDetail 初始化成功！");
        return jobDetail;
    }

    /**
     * 方法名：
     * 功能：配置定时任务的触发器，也就是什么时候触发执行定时任务
     * 描述：
     * 创建人：paracosm
     * 创建时间：2021/4/27 23:13
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    @Bean(name = "jobTrigger")
    public CronTriggerFactoryBean cronTriggerFactoryBean(MethodInvokingJobDetailFactoryBean jobDetail){
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(Objects.requireNonNull(jobDetail.getObject()));
        //初始化的cron表达式(每周五15点10分触发)
        trigger.setCronExpression(TASK_TIME);
//        trigger.setCronExpression("*/5 * * * * ?");//每五秒执行一次 test
        //trigger的name
        trigger.setName("myTigger");
        log.info("jobTrigger 初始化成功！");
        return trigger;
    }

    /**
     * 方法名：
     * 功能：定义quartz调度工厂
     * 描述：
     * 创建人：paracosm
     * 创建时间：2021/4/27 23:13
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    @Bean (name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean(Trigger trigger){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        //用于quartz集群，QuartzScheduler启动时更新已存在的job
        factoryBean.setOverwriteExistingJobs(true);
        //延时启动，应用启动5秒后
        factoryBean.setStartupDelay(5);
        //注册触发器
        factoryBean.setTriggers(trigger);
        log.info("scheduler 初始化成功！");
        return factoryBean;
    }

    /**
     * 方法名：
     * 功能：多任务时的Scheduler，动态设置Trigger。一个SchedulerFactoryBean可能会有多个Trigger
     * 描述：
     * 创建人：paracosm
     * 创建时间：2021/4/27 23:13
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    @Bean(name = "multitaskScheduler")
    public SchedulerFactoryBean schedulerFactoryBean(){
        return new SchedulerFactoryBean();
    }
}
