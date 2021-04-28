package com.nmsl.controller.admin;

import com.nmsl.entity.Blog;
import com.nmsl.common.CommonUrl;
import com.nmsl.entity.User;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import com.nmsl.service.TagService;
import com.nmsl.service.TypeService;
import com.nmsl.controller.model.BlogQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 后台博客管理
 * @Author Paracosm
 * @Date 2021/1/18 16:53
 * @Version 1.0
 */
@Controller
@RequestMapping("/admin")
@Api(tags = "后台博客管理模块")
public class BlogController {

    @Resource
    private BlogService blogService;

    @Resource
    private TypeService typeService;

    @Resource
    private TagService tagService;

    @Resource
    private CommentService commentService;

    /**
     * 博客信息
     */
    private void BLOG_MSG_NUM(Model model){
        model.addAttribute("blogNum", blogService.listBlog());
        model.addAttribute("commentNum", commentService.listComment());
    }

    /**
     * 进入博客管理
     */
    @GetMapping("/blogs")
    @ApiOperation(value = "博客信息")
    public String blogs(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));

        BLOG_MSG_NUM(model);

        return CommonUrl.BLOGS;
    }

    /**
     * 搜索
     */
    @PostMapping("/blogs/search")
    @ApiOperation(value = "根据关键字搜索")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        BLOG_MSG_NUM(model);
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }


    /**
     * 博客内容
     */
    @GetMapping("/blogs/input")
    @ApiOperation(value = "查看博客内容")
    public String input(Model model){

        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());


        return CommonUrl.BLOGS_INPUT;
    }



    /**
     * 编辑博客
     */
    @GetMapping("/blogs/{id}/input")
    @ApiOperation(value = "根据id编辑博客")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);

        return CommonUrl.BLOGS_INPUT;
    }

    /**
     * 编辑博客内容
     */
    @PostMapping("/blogs")
    @ApiOperation(value = "编辑博客内容")
    public String post(@Valid Blog blog, HttpSession session, RedirectAttributes attributes) {

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b;

        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null) {
            //没保存成功
            attributes.addFlashAttribute("msg", "操作失败,请重试!");

        } else {
            //保存成功
            attributes.addFlashAttribute("msg", "操作成功!");
        }

        return CommonUrl.BLOGS_REDIRECT;
    }


    @GetMapping("/blogs/{id}/delete")
    @ApiOperation(value = "根据id删除博客")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){

//        List<Comment> comments = commentService.listCommentByBlogId(id);
//        if (comments != null) {
//            //如果该博客底下有评论，则级联删除所有评论，才可以删除博客
//        }

        if (id == null) {
            attributes.addFlashAttribute("msg", "操作失败,请重试!");
        } else {
            blogService.deleteBlog(id);
            attributes.addFlashAttribute("msg", "删除成功!");
        }
        return CommonUrl.BLOGS_REDIRECT;
    }


    private void setTypeAndTag(Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }


}
