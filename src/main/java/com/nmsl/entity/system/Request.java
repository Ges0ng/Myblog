package com.nmsl.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author paracosm
 * @version 1.0
 * @date 2021/4/26 17:25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "sys_request_log")
public class Request {

    /**
     * 自增id 主键
     * GeneratedValue 生成策略
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发出请求的url
     */
    private String url;

    /**
     * 访问者ip
     */
    private String ip;

    /**
     * 转换的真实地址
     * */
    private String addr;

    /**
     * 调用方法 classMethod
     * */
    private String classMethod;

    /**
     * 创建时间 （对应数据库生成时间的类型）
     * */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
}
