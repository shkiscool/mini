package com.dazhao.pojo.dao;

import lombok.Data;

/**
 * 权限表
 */
@Data
public class Permission {

    /**
     * 权限id
     */
    private Integer id;

    /**
     * 访问路径
     */
    private String permission;
}
