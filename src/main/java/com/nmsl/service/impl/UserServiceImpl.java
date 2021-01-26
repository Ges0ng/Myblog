package com.nmsl.service.impl;

import com.nmsl.dao.UserRepository;
import com.nmsl.domain.User;
import com.nmsl.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Paracosm
 * @Date 2021/1/18 16:46
 * @Version 1.0
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;


    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return user;
    }
}
