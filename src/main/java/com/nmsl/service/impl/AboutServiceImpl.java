package com.nmsl.service.impl;

import com.nmsl.cache.RedisCache;
import com.nmsl.dao.AboutRepository;

import com.nmsl.entity.About;
import com.nmsl.service.AboutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自我介绍
 * @Author Paracosm
 * @Date 2021/2/6 0:24
 * @Version 1.0
 */
@Service
@Slf4j
public class AboutServiceImpl implements AboutService {

    @Resource
    private AboutRepository aboutRepository;

    @Resource
    private RedisCache redisCache;

    @Override
    public List<About> listAbout() {
        //about缓存名字
        String aboutCache = "aboutCache";

        //如果缓存存在
        if (redisCache.getCacheObject(aboutCache) != null) {
            return redisCache.getCacheObject("aboutCache");
        }
        //如果缓存不存在则设置缓存，过期时间
        List<About> allAbout = aboutRepository.findAll();
        redisCache.setCacheObject("aboutCache", allAbout);
        redisCache.expire("aboutCache", 7, TimeUnit.DAYS);
        return allAbout;
    }
}
