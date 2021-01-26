package com.nmsl.controller.admin;

import com.nmsl.domain.User;
import com.nmsl.service.*;
import com.nmsl.utils.Md5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author Paracosm
 * @Date 2021/1/18 16:53
 * @Version 1.0
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    private String LOGIN = "admin/login";
    private String REDIRECT_ADMIN = "redirect:/admin";

    @Resource
    private UserService userService;

    @Resource
    private BlogService blogService;

    @Resource
    private TypeService typeService;

    @Resource
    private TagService tagService;



    @GetMapping
    public String loginPage(){
        return LOGIN;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model) {
        User user = userService.checkUser(username, Md5Utils.string2Md5(password));
        if (user != null) {
            //验证成功后将密码设为null再传回前台
            user.setPassword(null);
            session.setAttribute("user",user);

            model.addAttribute("blogNum", blogService.listBlog());
            model.addAttribute("tagNum", tagService.listTag());
            model.addAttribute("typeNum", typeService.listType());

            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return REDIRECT_ADMIN;
        }       }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return REDIRECT_ADMIN;
    }

}
