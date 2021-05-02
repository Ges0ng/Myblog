package com.nmsl.service.impl;

import com.nmsl.cache.RedisCache;
import com.nmsl.exception.NotFoundException;
import com.nmsl.dao.BlogRepository;
import com.nmsl.entity.Blog;
import com.nmsl.entity.Type;
import com.nmsl.service.BlogService;
import com.nmsl.utils.MarkdownUtils;
import com.nmsl.utils.common.MyBeanUtils;
import com.nmsl.controller.model.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author Paracosm
 * @Date 2021/1/21 19:16
 * @Version 1.0
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogRepository blogRepository;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询blog对象
     */
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    /**
     * 更新
     */
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).get();

        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }

        //三十分钟内重复浏览不算
        if (redisCache.getCacheObject("updateView_"+id) == null) {
            blogRepository.updateViews(id);
            redisCache.setCacheObject("updateView_"+id,id,30,TimeUnit.MINUTES);
        }

        Blog blogCache = redisCache.getCacheObject("blog_" + id);
        //如果缓存为空则设置一个一分钟的缓存，如果不为空直接返回缓存结果
        if (blogCache == null) {
            redisCache.setCacheObject("blog_" + id, blog, 1, TimeUnit.MINUTES);
            Blog b = new Blog();
            BeanUtils.copyProperties(blog, b);
            String content = b.getContent();
            blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

            return blog;
        } else {
            Blog b = new Blog();
            BeanUtils.copyProperties(blogCache,b);
            String content =b.getContent();
            blogCache.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

            return blogCache;
        }

    }


    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    /**
     * 分页查询blog,自动拼接动态查询的sql语句
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll((Specification<Blog>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            /*标题*/
            if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + blog.getTitle() + "%"));
            }
            /*分类*/
            if (blog.getTypeId() != null) {
                predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
            }
            /*是否推荐*/
            if (blog.isRecommend()) {
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
            }

            query.where(predicates.toArray(new Predicate[predicates.size()]));
            return null;
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll((Specification<Blog>) (root, query, criteriaBuilder) -> {
            Join<Object, Object> join = root.join("tags");
            return criteriaBuilder.equal(join.get("id"),tagId);
        },pageable);
    }

    /**
     * 新增blog
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    /**
     * 更新blog信息
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();

        if (b == null){
            throw new NotFoundException("该博客不存在");
        }

        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());


        return blogRepository.save(b);
    }

    /**
     * 删除
     */
    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        return blogRepository.findTop(pageable);

    }

    @Override
//    @Cacheable(cacheNames = "findBlogAll")
    public List<Blog> listBlog() {


        return blogRepository.findAll();
    }

    /**
     * 归档
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map  = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }



        return map;
    }

    /**
     * 所有查看
     */
    @Override
    public int allViews () {
        return blogRepository.findAllByViews();
    }

}
