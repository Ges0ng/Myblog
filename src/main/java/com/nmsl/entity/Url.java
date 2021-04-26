package com.nmsl.entity;

/**
 * 固定url
 * @author paracosm
 * @version 1.0
 * @date 2021/4/23 23:16
 */
public final class Url {

    /**
     * redirect 重定向
     */
    public static final String REDIRECT = "redirect:/";

    /**
     * ADMIN 管理员
     */
    public static final String ADMIN = "admin/";

    /**
     * blog 博客
     */
    public static final String BLOGS = ADMIN + "blogs";
    public static final String BLOGS_INPUT = ADMIN + "blogs-input";
    public static final String BLOGS_REDIRECT = REDIRECT + BLOGS;

    /**
     * login 登录
     */
    public static final String LOGIN = ADMIN + "login";
    public static final String REDIRECT_ADMIN = REDIRECT + "admin";

    /**
     * Tag 标签
     */
    public static final String TAGS = ADMIN + "tags";
    public static final String TAGS_INPUT = ADMIN + "tags-input";
    public static final String TAGS_REDIRECT = REDIRECT + ADMIN + "tags";

    /**
     * type 类型
     */
    public static final String TYPES = ADMIN + "types";
    public static final String TYPES_INPUT = ADMIN + "types-input";
    public static final String TYPES_REDIRECT = REDIRECT + ADMIN + "types";

    /**
     * about 关于我
     */
    public static final String ABOUT = "about";

    /**
     * archives 归档
     */
    public static final String ARCHIVES = "archives";

    /**
     * comment 评论
     */
    public static final String COMMENTS = REDIRECT + "comments";

    /**
     * 首页
     */
    public static final String INDEX = "index";
    public static final String SEARCH = "search";

    /**
     * 音乐
     */
    public static final String MUSIC = "music";
}
