package com.nmsl.dao;


import com.nmsl.entity.About;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 关于我页面，自我介绍
 * @Author Paracosm
 * @Date 2021/2/5 23:44
 * @Version 1.0
 */
public interface AboutRepository extends JpaRepository<About,Long> {
}
