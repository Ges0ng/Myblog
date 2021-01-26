package com.nmsl.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截
 * @Author Paracosm
 * @Date 2021/1/18 19:44
 * @Version 1.0
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

            if (request.getSession().getAttribute("user") == null){
                response.sendRedirect("/admin");
                request.setAttribute("message","请登录后再尝试");
                return false;
            }
        return true;
    }
}
