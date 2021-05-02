package com.nmsl.controller;

import com.nmsl.controller.common.CommonCache;
import com.nmsl.entity.Comment;
import com.nmsl.common.CommonUrl;
import com.nmsl.entity.User;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import com.nmsl.utils.MailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

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

    @Resource
    private CommonCache commonCache;

    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 默认的通知邮箱
     */
    private String toMail = "1473713606@qq.com";

    /**
     * 评论内容局部刷新
     */
    @GetMapping("/comments/{blogId}")
    @ApiOperation(value = "查询博客下的评论")
    public String comments(@PathVariable Long blogId, Model model) {
        commonCache.blogMsg(model);
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    @ApiOperation(value = "评论")
    public String post(Comment comment, HttpSession session) throws MessagingException, GeneralSecurityException, UnsupportedEncodingException {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));

        /*评论时候的判断是否是管理员*/
        User user = (User) session.getAttribute("user");
        if (user != null) {
            toMail = user.getEmail();

            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);

        //发送邮件通知
        MailUtil mailUtil = new MailUtil();
        //假如回复的用户存在，则发邮件通知被回复用户
        if (comment.getParentComment() != null) {
            toMail = comment.getParentComment().getEmail();
        }
        //发送邮件
        mailUtil.sendMail("您的博客有新评论","您有新的评论："+comment.getContent(), toMail);

        return CommonUrl.COMMENTS + "/" + blogId;
    }

}
