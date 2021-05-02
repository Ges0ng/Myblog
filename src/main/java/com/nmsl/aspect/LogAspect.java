package com.nmsl.aspect;

import com.nmsl.cache.RedisCache;
import com.nmsl.controller.common.CommonCache;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private RedisCache redisCache;

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
        //获取请求属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        /*url*/
        String url = Objects.requireNonNull(request).getRequestURL().toString();
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

        /*三十分钟内不记录同一ip重复操作到数据库*/
        if (redisCache.getCacheObject("ip_" + ip) == null || redisCache.getCacheObject("url_" + url) == null) {
            //保存日志到数据库
            Request requestEntity = new Request();
            requestEntity.setUrl(url);
            requestEntity.setIp(ip);
            requestEntity.setAddr(adr);
            requestEntity.setClassMethod(classMethod);
            proxy.saveLog(requestEntity);
            /*将ip和调用的接口记录到缓存，以免重复记录*/
            redisCache.setCacheObject("ip_" + ip, ip, 30, TimeUnit.MINUTES);
            redisCache.setCacheObject("url_" + url, url, 30, TimeUnit.MINUTES);
            log.info("=====将ip或url存入缓存,30分钟后销毁=====");
        }

    }


    /**
     * 方法执行完返回之后拦截 声明后置通知
     *     @AfterReturning(returning = "result",pointcut = "log()") 确定返回的参数,切面方法
     * */
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
//        log.info("Result:{}",result);
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
    private static class RequestLog{
        private final String url;
        private final String ip;
        private final String addr;
        private final String classMethod;
        private final Object[] args;

    }


}
