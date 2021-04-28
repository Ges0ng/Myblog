package com.nmsl.common;

import com.nmsl.service.RequestService;
import com.nmsl.utils.common.SystemInfoUtils;
import com.nmsl.utils.quartz.MysqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
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
@Slf4j
public class DatabaseBakTimingTask {

    @Resource
    private RequestService proxy;

    /**
     * 是否开启备份 默认开
     */
    public static final Boolean BAK_ENABLE = true;

    /**
     * 每周五执行一次定时任务===>备份数据库
     */
    public void start() throws InterruptedException, UnknownHostException {
        //启动备份
        if (BAK_ENABLE) {
            new MysqlUtil().exportDataBase();
            log.info("备份数据库成功，时间为 : {}", SystemInfoUtils.getCurrentTime());
            log.info("当前系统信息 : {}", SystemInfoUtils.getInfo());
        }
        //如果大于10000条则清空
        if (proxy.truncateLog()) {
            log.info("清空日志数据 : {}", "数据库日志记录超过10000条，删除成功");
        }
    }



}
