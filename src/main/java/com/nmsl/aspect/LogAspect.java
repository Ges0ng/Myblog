package com.nmsl.aspect;

import com.nmsl.entity.system.Request;
import com.nmsl.service.RequestService;
import com.nmsl.utils.ip.AddressUtils;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author Paracosm
 * @Date 2020/12/30 20:45
 * @Version 1.0
 * @Aspect 切面
 * @Component 组件扫描
 */

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private RequestService proxy;

    /**
     * 定义一个切面
    * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern)throws-pattern?)
    * Controller下的所有控制器,无论私有共有的类的所有方法
    * */

    @Pointcut("execution(* com.nmsl.controller.*.*(..))")
    public void log(){

    }


    /**方法之前
    * @After("log()") 切面方法
    * */

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        /*url*/
        String url = request.getRequestURL().toString();
        /*ip地址*/
        String ip = request.getRemoteAddr();
        /*真实地址*/
        String adr = AddressUtils.getRealAddressByIP(ip);

        /*
        * joinPoint.getSignature().getDeclaringTypeName() 声明类型名称
        * joinPoint.getSignature().getName(); 方法的名字
        */
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        /*参数*/
        Object[] args = joinPoint.getArgs();
        /*输出日志*/
        RequestLog requestLog = new RequestLog(url, ip, adr, classMethod, args);
        log.info("Request : {}", requestLog);

        //保存日志到数据库
        Request requestEntity = new Request();
        requestEntity.setUrl(url);
        requestEntity.setIp(ip);
        requestEntity.setAddr(adr);
        requestEntity.setClassMethod(classMethod);
        if (proxy.truncateLog()) {
            log.info("请求参数日志 : {}", "数据库日志记录超过10000条，删除成功");
        }
        proxy.saveLog(requestEntity);
    }


    /**
     * 方法执行完返回之后拦截 声明后置通知
     *     @AfterReturning(returning = "result",pointcut = "log()") 确定返回的参数,切面方法
     * */
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        log.info("Result:{}",result);
    }


    /*** 方法之后 最终声明
    * @After("log()") 切面方法
    *
    * */
    @After("log()")
    public void doAfter(){
        /*log.info("--------doAfter---------");*/
    }


    /**
     * 记录日志内容
     *
     * 1.请求url
     *
     * 2.访问者ip
     *
     * 3.调用方法 classMethod
     *
     * 4.参数args
     *
     * 5.返回内容
     */
    @ToString
    @AllArgsConstructor
    private class RequestLog{
        private String url;
        private String ip;
        private String addr;
        private String classMethod;
        private Object[] args;

    }


}
