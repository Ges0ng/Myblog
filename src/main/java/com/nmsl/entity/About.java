package com.nmsl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author Paracosm
 * @Date 2021/2/5 23:36
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //用的是jpa
@Table(name = "t_about")
public class About {
    /**
     * 自增id 主键
     * GeneratedValue 生成策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 自我介绍
     */
    private String myIntroduction;

}
