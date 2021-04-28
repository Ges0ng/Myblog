package com.nmsl.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * query
 * @Author Paracosm
 * @Date 2021/1/21 22:36
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommend;

}
