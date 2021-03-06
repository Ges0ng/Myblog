package com.nmsl.dao;

import com.nmsl.entity.Blog;
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
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

        /**
         * 是否推荐
         */
        @Query("select b from Blog b where b.recommend = true")
        List<Blog> findTop(Pageable pageable);

        /**
         * 根据标题或者内容查询
         */
        @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
        Page<Blog> findByQuery(String query,Pageable pageable);

        /**
         * 点击量+1
         */
        @Transactional
        @Modifying
        @Query("update Blog b set b.views=b.views+1 where b.id=?1")
        int updateViews(Long id);

        /**
         * 年份归档
         */
        @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year DESC ")
        List<String> findGroupYear();

        /**
         * 年份
         */
        @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
        List<Blog> findByYear(String year);

        /**
         * 总点击率
         */
        @Query("select sum(b.views) from Blog b")
        int findAllByViews();
}
