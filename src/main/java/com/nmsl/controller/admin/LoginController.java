package com.nmsl.controller.admin;

import com.nmsl.common.CommonUrl;
import com.nmsl.entity.User;
import com.nmsl.service.*;
import com.nmsl.utils.Md5Utils;
import com.nmsl.utils.ip.AddressUtils;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 后台登录模块
 * @Author Paracosm
 * @Date 2021/1/18 16:53
 * @Version 1.0
 */
@Controller
@RequestMapping("/admin")
@Api(tags = "后台管理登录模块")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private BlogService blogService;

    @Resource
    private TypeService typeService;

    @Resource
    private TagService tagService;


    /**
     * 进入登录页面
     * @return
     */
    @GetMapping
    @ApiOperation(value = "进入登录页面")
    public String loginPage(Model model){
        model.addAttribute("adr", AddressUtils.getAddr());
        return CommonUrl.LOGIN;
    }
    /**
     * 管理员登录验证
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @param model
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登陆验证")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String verCode,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model,
                        HttpServletRequest request) {

        //校对验证码
        if (!CaptchaUtil.ver(verCode, request)) {
            CaptchaUtil.clear(request);  // 清除session中的验证码
            attributes.addFlashAttribute("message", "验证码不正确");
            return CommonUrl.REDIRECT_ADMIN;
        }

        //校对用户
        User user = userService.checkUser(username, Md5Utils.string2Md5(password));
        if (user != null) {
            session.setAttribute("user",user);

            BLOG_MSG_NUM(model);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return CommonUrl.REDIRECT_ADMIN;
        }
    }

    /**
     * 用户注销
     * @param session
     * @return
     */
    @GetMapping("/logout")
    @ApiOperation(value = "用户注销")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return CommonUrl.REDIRECT_ADMIN;
    }

    //博客相关信息
    private void BLOG_MSG_NUM(Model model){
        model.addAttribute("blogNum", blogService.listBlog());
        model.addAttribute("tagNum", tagService.listTag());
        model.addAttribute("typeNum", typeService.listType());
    }

}
