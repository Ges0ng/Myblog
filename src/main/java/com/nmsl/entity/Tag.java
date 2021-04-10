package com.nmsl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/15 20:13
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //用的是jpa
@Table(name = "t_tag")
public class Tag  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //生成策略 //生成策略
    private Long id;

    /**
     * 标签名
     * @NotBlank 非空校验
     */
    @NotBlank(message = "分类名称不能为空!")
    private String name;


    /**
     * 两边都是多的一端，但还是要指定一下哪边是维护的关系，哪边是被维护的关系，tag是被维护关系
     */
    @ManyToMany(mappedBy = "tags")
    private List<Blog> blogs = new ArrayList<>();

}
