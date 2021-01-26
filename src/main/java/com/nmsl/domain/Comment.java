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
 * @Date 2021/1/15 20:14
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //用的是jpa
@Table(name = "t_comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //生成策略 //生成策略
    private Long id;
    /*昵称*/
    private String nickname;
    /*邮箱*/
    private String email;

    /*评论的内容*/
    private String content;

    /*头像*/
    private String avatar;

    /*创建时间*/
    @Temporal(TemporalType.TIMESTAMP) /*对应数据库生成时间的类型*/
    private Date createTime;

    @ManyToOne
    private Blog blog;

    @OneToMany(mappedBy = "parentComment")  /*父级对象, 一个父类对应多个子类*/
    private List<Comment> replyComments = new ArrayList<>();

    @ManyToOne
    private Comment parentComment;

    private boolean adminComment;

 }
