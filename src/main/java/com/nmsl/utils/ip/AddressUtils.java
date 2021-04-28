package com.nmsl.utils.ip;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.utils.StringUtils;
import com.nmsl.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;

/**
 * 获取地址类
 * 
 * @author paracosm
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    /**
     * IP地址查询
     */
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    /**
     * 未知地址
     */
    public static final String UNKNOWN = "XX XX";

    /**
     * 真实地址
     */
    public static String getAddr() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        /*ip地址*/
//        String ip = request != null ? request.getRemoteAddr() : null;

        IpInfoUtil ip = new IpInfoUtil();
        String ipAddr = ip.getIpAddr(Objects.requireNonNull(request));
        /*真实地址*/
        String realAddressByIP = getRealAddressByIP(ipAddr);
        return realAddressByIP;
    }

    public static String getRealAddressByIP(String ip)
    {
        String address = UNKNOWN;
        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }
            try
            {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", "GBK");
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
            catch (Exception e)
            {
                log.error("获取地理位置异常 {}", ip);
            }
        return address;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }
}
