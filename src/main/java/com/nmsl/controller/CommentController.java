package com.nmsl.controller;

import com.nmsl.entity.Comment;
import com.nmsl.entity.User;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 评论
 * @Author Paracosm
 * @Date 2021/1/25 14:22
 * @Version 1.0
 */
@Controller
@Api(tags = "评论模块")
public class CommentController {
    
    @Resource
    private CommentService commentService;

    @Resource
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 博客信息
     * @param model
     */
    private void BLOG_MSG_NUM(Model model){
        model.addAttribute("blogNum", blogService.listBlog());
        model.addAttribute("commentNum", commentService.listComment());
    }

    /**
     * 评论内容局部刷新
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/comments/{blogId}")
    @ApiOperation(value = "查询博客下的评论")
    public String comments(@PathVariable Long blogId, Model model){
        BLOG_MSG_NUM(model);
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    @ApiOperation(value = "评论")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));

        /*评论时候的管理员判断*/
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }



}
