package com.nmsl.entity;

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
@Entity
@Table(name = "t_comment")
public class Comment implements Serializable {
    /**
     * 自增id 主键
     * GeneratedValue 生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 昵称
     * */
    private String nickname;
    /**
     * 邮箱
     * */
    private String email;

    /**
     * 评论的内容
     * */
    private String content;

    /**
     * 头像
     * */
    private String avatar;

    /**
     * 创建时间
     * */
    @Temporal(TemporalType.TIMESTAMP) /*对应数据库生成时间的类型*/
    private Date createTime;

    /**
     * bolg类
     */
    @ManyToOne
    private Blog blog;

    /**
     * 父级对象, 一个父类对应多个子类
     * */
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>();

    /**
     * 上级评论
     */
    @ManyToOne
    private Comment parentComment;

    /**
     * 管理员评论
     */
    private boolean adminComment;

 }
