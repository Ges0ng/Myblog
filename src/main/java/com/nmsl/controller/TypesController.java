package com.nmsl.controller;

import com.nmsl.controller.common.CommonCache;
import com.nmsl.entity.Type;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import com.nmsl.service.TypeService;
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
 * 分类
 * @Author Paracosm
 * @Date 2021/1/25 20:58
 * @Version 1.0
 */
@Controller
@Api(tags = "类型模块")
public class TypesController {

    @Resource
    private TypeService typeService;

    @Resource
    private BlogService blogService;

    @Resource
    private CommonCache commonCache;

    @GetMapping("/types/{id}")
    @ApiOperation("根据id查看类型")
    public String types(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        commonCache.blogMsg(model);

        List<Type> types = typeService.listTypeToTop(10000);
        if (id == -1) {
            id = types.get(0).getId();
        }

        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);

        model.addAttribute("types", types);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("actionTypeId", id);

        return "types";
    }

}
