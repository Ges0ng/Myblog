package com.nmsl.dao;

import com.nmsl.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/25 14:29
 * @Version 1.0
 */

public interface CommentRepository extends JpaRepository<Comment,Long> {

    /**
     * 评论
     */
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);


}
