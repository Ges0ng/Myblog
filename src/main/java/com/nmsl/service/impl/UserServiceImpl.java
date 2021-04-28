package com.nmsl.service.impl;

import com.nmsl.dao.UserRepository;
import com.nmsl.entity.User;
import com.nmsl.service.UserService;
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

    /**
     * 验证登录
     */
    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);

        //验证成功后将密码设为null再传回前台
        user.setPassword(null);

        return user;
    }
}
