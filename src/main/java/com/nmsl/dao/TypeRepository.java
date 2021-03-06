package com.nmsl.dao;

import com.nmsl.entity.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/19 17:14
 * @Version 1.0
 */
public interface TypeRepository extends JpaRepository<Type,Long> {

    /**
     * 根据名称查询
     */
    Type findByName(String name);

    /**
     * 自定义查询
     */
    @Query("select t from Type t ")
    List<Type> findTop(Pageable pageable);

}
