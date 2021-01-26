package com.nmsl.controller;

import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @Author Paracosm
 * @Date 2021/1/25 22:07
 * @Version 1.0
 */
@Controller
public class MusicController {

    @Resource
    private BlogService blogService;

    @Resource
    private CommentService commentService;

    /**
     * 博客信息
     * @param model
     */
    private void BLOG_MSG_NUM(Model model){
        model.addAttribute("blogNum", blogService.listBlog());
        model.addAttribute("commentNum", commentService.listComment());
    }

    @GetMapping("/music")
    public String music(Model model){
        BLOG_MSG_NUM(model);
        return "music";
    }
}
