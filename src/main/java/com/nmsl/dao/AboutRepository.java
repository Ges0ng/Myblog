package com.nmsl.dao;


import com.nmsl.entity.About;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Paracosm
 * @Date 2021/2/5 23:44
 * @Version 1.0
 */
public interface AboutRepository extends JpaRepository<About,Long> {
}
