package com.nmsl.dao;

import com.nmsl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Paracosm
 * @Date 2021/1/18 16:48
 * @Version 1.0
 */


public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * 比对账号密码是否正确
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
