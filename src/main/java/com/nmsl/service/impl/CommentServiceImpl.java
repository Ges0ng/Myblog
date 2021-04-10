package com.nmsl.service.impl;

import com.nmsl.dao.CommentRepository;
import com.nmsl.entity.Comment;
import com.nmsl.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/25 14:28
 * @Version 1.0
 */

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,Sort.by(Sort.Direction.ASC,"createTime"));
        return eachComment(comments);
    }

    @Override
    @Transient
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        } else {
            comment.setParentComment(null);
        }

        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> listComment() {
        return commentRepository.findAll();
    }

    /**
     * 循环每个顶级的节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment: comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }

        //合并评论的各层子代到第一级子集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * root根节点,blog不为空的对象集合
     * @param comments
     */
    private void combineChildren(List<Comment> comments){
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1: replys1) {
                //循环迭代,找出子代,存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合,相当于一个公共容器
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * 递归迭代
     * @param comment
     */
    private void recursively(Comment comment) {
        //顶节点添加到临时存放的集合
        tempReplys.add(comment);

        if(comment.getReplyComments().size() > 0){
            List<Comment> replays = comment.getReplyComments();
            for (Comment replay : replays) {
                recursively(replay);
            }
        }

    }
}
