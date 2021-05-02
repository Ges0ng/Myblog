package com.nmsl.controller;

import com.nmsl.controller.common.CommonCache;
import com.nmsl.service.BlogService;
import com.nmsl.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 归档页面
 * @Author Paracosm
 * @Date 2021/1/25 23:03
 * @Version 1.0
 */
@Controller
@Api(tags = "归档模块")
public class ArchiveController {

    @Resource
    private BlogService blogService;

    @Resource
    private CommonCache commonCache;

    @GetMapping("/archives")
    @ApiOperation(value = "跳转到‘归档信息’页面")
    public String archive(Model model) {
        model.addAttribute("archivesMap", blogService.archiveBlog());
        commonCache.blogMsg(model);
        return "archives";
    }


}
