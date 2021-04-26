package com.nmsl.controller;

import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import com.nmsl.service.TagService;
import com.nmsl.service.TypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * 首页
 * @Author Paracosm
 * @Date 2021/1/13 17:26
 * @Version 1.0
 */
@Controller
@Api(tags = "首页模块")
public class IndexController {

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
        model.addAttribute("viewsNum", blogService.allViews());
        model.addAttribute("commentNum", commentService.listComment());
    }

    /**
     * 首页
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(value = {"/","/index"})
    @ApiOperation(value = "跳转到博客首页")
    public String index(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        BLOG_MSG_NUM(model);

        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeToTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }

    /**
     * 搜索
     * @param pageable
     * @param query
     * @param model
     * @return
     */
    @PostMapping("/search")
    @ApiOperation(value = "根绝关键字搜索")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        BLOG_MSG_NUM(model);

        model.addAttribute("page",blogService.listBlog("%"+query+"%", pageable) );
        model.addAttribute("query",query);

        return "search";
    }

    /**
     * 根据id进入博客
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping ("/blog/{id}")
    @ApiOperation (value = "根据id查看博客信息")
    public String blog (@PathVariable Long id, Model model) {
        BLOG_MSG_NUM(model);

        if (!blogService.getBlog(id).isPublished()) {
            model.addAttribute("msg", "草稿内容暂时无法观看");
            return "index";
        } else {
            model.addAttribute("blog", blogService.getAndConvert(id));
        }
        return "blog";
    }



}
