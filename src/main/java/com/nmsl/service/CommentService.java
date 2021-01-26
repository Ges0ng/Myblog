package com.nmsl.service;

import com.nmsl.domain.Comment;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/25 14:27
 * @Version 1.0
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    List<Comment> listComment();
}
