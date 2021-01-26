package com.nmsl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/15 20:18
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //用的是jpa
@Table(name = "t_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //生成策略
    private Long id;

    /*昵称*/
    private String nickname;

    private String username;

    private String password;

    private String email;

    private String avatar;

    /*用户类型*/
    private Integer type;

    @Temporal(TemporalType.TIMESTAMP) /*对应数据库生成时间的类型*/
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP) /*对应数据库生成时间的类型*/
    private Date updateTime;

    @OneToMany(mappedBy = "user")   /*关系被维护方*/
    public List<Blog> blogs = new ArrayList<>();
}
