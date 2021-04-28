package com.nmsl.dao;

import com.nmsl.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/20 02:24
 * @Version 1.0
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

    /**
     * 根据名称查询
     */
    Tag findByName(String name);

    /**
     * 分页
     */
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);

}
