package com.nmsl.controller.common;

import com.nmsl.cache.RedisCache;
import com.nmsl.entity.Blog;
import com.nmsl.entity.Comment;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author paracosm
 * @version 1.0
 * @date 2021/5/1 18:26
 */

@Component
public class CommonCache {
    @Resource
    private BlogService blogService;

    @Resource
    private CommentService commentService;

    @Resource
    private RedisCache redisCache;

    /**
     * 博客数量缓存名
     */
    private final static String BLOG_NUM_CACHE = "blogNumCache";
    /**
     * 评论数量缓存名
     */
    private final static String COMMENT_NUM_CACHE = "commentNumCache";


    public void blogMsg (Model model) {

        //博客数量
        if (redisCache.getCacheObject(BLOG_NUM_CACHE) != null) {
            model.addAttribute("blogNum", redisCache.getCacheObject(BLOG_NUM_CACHE));
        } else {
            List<Blog> blogs = blogService.listBlog();
            redisCache.setCacheObject(BLOG_NUM_CACHE, blogs,1,TimeUnit.DAYS);
            model.addAttribute("blogNum", blogs);
        }

        //评论数量
        if (redisCache.getCacheObject(COMMENT_NUM_CACHE) != null) {
            model.addAttribute("commentNum", redisCache.getCacheObject(COMMENT_NUM_CACHE));
        } else {
            List<Comment> comments = commentService.listComment();
            redisCache.setCacheObject(COMMENT_NUM_CACHE, comments,1, TimeUnit.DAYS);
            model.addAttribute("commentNum", commentService.listComment());
        }

        //来访者数量
        model.addAttribute("viewsNum", blogService.allViews());
    }
}
