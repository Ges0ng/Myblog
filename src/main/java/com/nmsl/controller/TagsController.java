package com.nmsl.controller;

import com.nmsl.entity.Tag;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import com.nmsl.service.TagService;
import com.nmsl.controller.model.BlogQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签
 * @Author Paracosm
 * @Date 2021/1/25 20:58
 * @Version 1.0
 */
@Controller
@Api(tags = "标签模块")
public class TagsController {

    @Resource
    private TagService tagService;

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
        model.addAttribute("viewsNum", blogService.allViews());
        model.addAttribute("commentNum", commentService.listComment());
    }

    @GetMapping("/tags/{id}")
    @ApiOperation("根据标签跳转")
    public String tags(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        BLOG_MSG_NUM(model);

        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }

        BlogQuery blogQuery = new BlogQuery();

        model.addAttribute("tags", tags);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("actionTagId", id);

        return "tags";
    }

}
