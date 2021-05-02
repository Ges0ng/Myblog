package com.nmsl.controller;

import com.nmsl.controller.common.CommonCache;
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
    private AboutService aboutService;

    @Resource
    private CommonCache commonCache;

    /**
     * 跳转到关于我页面
     */
    @GetMapping("/about")
    @ApiOperation(value = "跳转到'关于我'页面")
    public String about(Model model){
        model.addAttribute("aboutText", aboutService.listAbout());
        commonCache.blogMsg(model);
        return "about";
    }

}
