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
@Entity //用的是jpa
@Table (name = "sys_request_log")
public class Request {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY) //生成策略
    private Long id;
    /*请求url*/
    private String url;
    /*访问者ip*/
    private String ip;
    /*真实地址*/
    private String addr;
    /*调用方法 classMethod*/
    private String classMethod;

    /*创建时间*/
    @Temporal(TemporalType.TIMESTAMP) /*对应数据库生成时间的类型*/
    private Date createTime;
}
