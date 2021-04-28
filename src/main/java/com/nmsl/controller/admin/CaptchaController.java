package com.nmsl.controller.admin;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

/**
 * @author 123
 * @version 1.0
 * @date 2021/4/21 22:10
 */

@Controller
@Api(tags = "验证码模块")
public class CaptchaController {

    /**
     * 混合型验证码
     */
    @RequestMapping("/captcha")
    @ApiOperation("验证码验证")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体 (有默认字体，可以不用设置)
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        // 验证码存入session
        request.getSession().setAttribute("captcha", specCaptcha.text().toLowerCase());

        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }


}
