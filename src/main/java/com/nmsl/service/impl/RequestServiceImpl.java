package com.nmsl.service.impl;

import com.nmsl.dao.RequestRepository;
import com.nmsl.entity.system.Request;
import com.nmsl.service.RequestService;
import com.nmsl.utils.common.SystemInfoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author 123
 * @version 1.0
 * @date 2021/4/26 17:35
 */
@Service
public class RequestServiceImpl implements RequestService {
    /**日志数量达到x以后全部清除，默认10000*/
    private static final int LOG_NUM = 10000;

    @Resource
    private RequestRepository requestRepository;

    @Value("${log.enable}")
    private boolean enable;

    /**
     * 保存日志
     */
    @Override
    public Request saveLog (Request request) {
        if (request.getId() == null) {
            request.setCreateTime(new Date());
        }
        return requestRepository.save(request);
    }

    /**
     * 查看所有日志
     */
    @Override
    public List<Request> listRequest () {
        return requestRepository.findAll();
    }

    /**
     * 返回日志数量
     */
    @Override
    public int logNum () {
        return requestRepository.findAllById();
    }

    /**
     * 达到一定数量后情况表数据
     */
    @Override
    public boolean truncateLog () {
        //如果数据量超过10000条则删除数据
        if (logNum() > LOG_NUM) {
            requestRepository.deleteAllById();
            return true;
        }
        return false;
    }
}
