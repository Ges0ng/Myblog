package com.nmsl.controller.admin;

import com.nmsl.domain.Blog;
import com.nmsl.domain.User;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import com.nmsl.service.TagService;
import com.nmsl.service.TypeService;
import com.nmsl.vo.BlogQuery;
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
 * @Author Paracosm
 * @Date 2021/1/18 16:53
 * @Version 1.0
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private String BLOGS = "admin/blogs";
    private String BLOGS_INPUT = "admin/blogs-input";
    private String BLOGS_REDIRECT = "redirect:/admin/blogs";

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
     * @param model
     */
    private void BLOG_MSG_NUM(Model model){
        model.addAttribute("blogNum", blogService.listBlog());
        model.addAttribute("commentNum", commentService.listComment());
    }

    /**
     * 进入博客管理
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));

        BLOG_MSG_NUM(model);

        return BLOGS;
    }

    /**
     * 搜索
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        BLOG_MSG_NUM(model);
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }


    /**
     * 博客内容
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model){

        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());


        return BLOGS_INPUT;
    }



    /**
     * 编辑博客
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);

        return BLOGS_INPUT;
    }

    /**
     * 编辑博客内容
     *
     * @return
     */
    @PostMapping("/blogs")
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

        return BLOGS_REDIRECT;
    }


    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("msg", "删除成功!");
        return BLOGS_REDIRECT;
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());

    }


}
