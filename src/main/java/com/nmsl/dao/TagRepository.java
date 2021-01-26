package com.nmsl.dao;

import com.nmsl.domain.Tag;
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

    Tag findByName(String name);


    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);

}
