package com.nmsl.dao;

import com.nmsl.entity.Blog;
import com.nmsl.entity.system.Request;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/20 02:24
 * @Version 1.0
 */
public interface RequestRepository extends JpaRepository<Request,Long>, JpaSpecificationExecutor<Request> {

    @Query("select count(id) from Request")
    int findAllById();

    @Transactional
    @Modifying
    @Query(value = "truncate table sys_request_log",nativeQuery = true)
    void deleteAllById ();
}
