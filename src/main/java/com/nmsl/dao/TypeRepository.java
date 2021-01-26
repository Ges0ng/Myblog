package com.nmsl.dao;

import com.nmsl.domain.Type;
import org.springframework.data.domain.Page;
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

    Type findByName(String name);

    /**
     * 自定义查询
     * @param pageable
     * @return
     */
    @Query("select t from Type t ")
    List<Type> findTop(Pageable pageable);

}
