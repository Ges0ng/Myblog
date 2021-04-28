package com.nmsl.common;

import com.nmsl.utils.quartz.MysqlUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务，备份
 * @author 123
 * @version 1.0
 * @date 2021/4/27 22:40
 */

@Configuration
@Component
@EnableScheduling
public class DatabaseBakTimingTask {

    /**
     * 每周五执行一次定时任务===>备份数据库
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        System.err.println("备份任务准备执行。"+new Date());
        new MysqlUtil().exportDataBase();
    }

}
