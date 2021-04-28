package com.nmsl.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 返回自定义的错误页面
 * @author Paracosm
 * @ControllerAdvice 拦截所有的标注有controller这个注解的控制器,
 * @ExceptionHandler 标识这个方法可以做异常处理的 Exception.class表示只要是exception级别的都可以
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    /*private Logger logger = LoggerFactory.getLogger(this.getClass());*/

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        /*记录*/
        log.error("Request URL : {}, Exception : {}", request.getRequestURI(),e.getMessage());


        /*通过注解工具找一个注解有没有存在,如果指定了状态就不应该拦截它 */
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }


        /*记录之后希望返回的错误页面*/
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURI());
        /*获取异常信息*/
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    }

}
