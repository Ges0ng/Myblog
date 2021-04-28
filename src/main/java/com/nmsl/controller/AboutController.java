package com.nmsl.controller;

import com.nmsl.service.AboutService;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 关于我
 * @Author Paracosm
 * @Date 2021/1/25 22:06
 * @Version 1.0
 */
@Api(tags = "关于我模块")
@Controller
public class AboutController {

    @Resource
    private BlogService blogService;


    @Resource
    private CommentService commentService;

    @Resource
    private AboutService aboutService;

    /**
     * 跳转到关于我页面
     */
    @GetMapping("/about")
    @ApiOperation(value = "跳转到'关于我'页面")
    public String about(Model model){
        model.addAttribute("aboutText", aboutService.listAbout());
        BLOG_MSG_NUM(model);
        return "about";
    }

    /**
     * 博客信息
     */
    private void BLOG_MSG_NUM(Model model){
        model.addAttribute("blogNum", blogService.listBlog());
        model.addAttribute("commentNum", commentService.listComment());
        model.addAttribute("viewsNum", blogService.allViews());
    }

}
