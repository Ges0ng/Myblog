package com.nmsl.domain;


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
 * @Date 2021/1/15 20:12
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //用的是jpa
@Table(name = "t_type")
public class Type implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //生成策略
    private Long id;

    /**
     * 分类的名字
     * @NotBlank 非空校验
     */
    @NotBlank(message = "分类名称不能为空!")
    private String name;


    /*说明当前type是被维护blog和type之间的关系,blog主动维护相互的关系*/
    @OneToMany(mappedBy = "type")
    public List<Blog> blogs = new ArrayList<>();
}
