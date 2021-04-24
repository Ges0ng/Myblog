package com.nmsl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/15 20:04
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //用的是jpa
@Table(name = "t_blog")
public class Blog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //生成策略
    private Long id;

    /*标题*/
    @NotBlank(message = "博客标题不能为空!")
    private String title;

    /*内容*/
    @NotBlank(message = "博客内容不能为空!")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    /*首图*/
    private String firstPicture;

    /*标记*/
    private String flag;

    /*浏览次数*/
    private Integer views;

    /*赞赏是否开启*/
    private boolean appreciation;

    /*转载声明是否开启*/
    private boolean shareStatement;

    /*评论是否开启*/
    private boolean commentabled;

    /*是否推荐*/
    private boolean recommend;

    /*是否发布还是保存草稿*/
    private boolean published;

    /*创建时间*/
    @Temporal(TemporalType.TIMESTAMP) /*对应数据库生成时间的类型*/
    private Date createTime;

    /*更新时间*/
    @Temporal(TemporalType.TIMESTAMP) /*对应数据库生成时间的类型*/
    private Date updateTime;


    @ManyToOne
    private Type type;  /*多的一端,关系维护端*/

    /**
     * 级联关系， 新增博客的时候连同新增一个tag
     */
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Tag> tags = new ArrayList<>(); /*两端都是多*/


    @ManyToOne  /*多个blog对一个user，关系的维护方*/
    private User user;


    @OneToMany(mappedBy = "blog")  /*一个blog包含多个comment*/
    private List<Comment> comments = new ArrayList<>();


    private String description;

    @Transient
    private String tagIds;

    public void init(){
        this.tagIds = tagsToIds(this.getTags());
    }

    /**
     * 将tags转换为tagIds 1,2,3
     */
    private String tagsToIds(List<Tag> tags){
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;

            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }

}
