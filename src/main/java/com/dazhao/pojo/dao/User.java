package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Table(name = "user")
@Builder
@AllArgsConstructor
public class User {

    /**
     * 管理员id
     */
    @ApiModelProperty(notes = "用户id")
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;

    /**
     * 管理员真实姓名
     */
    @ApiModelProperty(notes = "用户真实姓名")
    @Column(name = "name")
    private String name;

    /**
     * 登录名称
     */
    @ApiModelProperty(notes = "登录名称")
    @Column(name = "username")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(notes = "密码")
    @Column(name = "password")
    private String password;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ApiModelProperty(notes = "创建时间")
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ApiModelProperty(notes = "更新时间")
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 逻辑删除
     */
    @ApiModelProperty(notes = "逻辑删除")
    @Column(name = "deleted")
    private Integer deleted;

    /**
     * 具有角色名称
     */
    @ApiModelProperty(notes = "具有角色名称")
    @Column(name = "role_name")
    private String roleName;


    public User() {
    }


}
