package com.nmsl.service;

import com.nmsl.entity.User;

/**
 * @Author Paracosm
 * @Date 2021/1/18 16:45
 * @Version 1.0
 */
public interface UserService {

    /**
     * 配对用户
     * @param username
     * @param password
     * @return
     */
    User checkUser(String username, String password);
}
