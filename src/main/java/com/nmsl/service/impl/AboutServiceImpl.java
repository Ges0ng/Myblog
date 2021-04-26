package com.nmsl.service.impl;

import com.nmsl.dao.AboutRepository;

import com.nmsl.entity.About;
import com.nmsl.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/2/6 0:24
 * @Version 1.0
 */
@Service
public class AboutServiceImpl implements AboutService {

    @Autowired
    private AboutRepository aboutRepository;

    @Override
    public List<About> listAbout() {
        return aboutRepository.findAll();
    }
}
