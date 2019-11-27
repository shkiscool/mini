package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作日志
 */
@Table(name = "operation_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog {

    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 操作IP
     */
    @Column(name = "ip")
    private String ip;
    /**
     * 操作模块
     */
    @Column(name = "module")
    private String module;

    /**
     * 操作内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @Transient
    private String operationName;


    /**
     * 有参构造
     */
    public OperationLog(Integer userId, String ip, String module, String content, Date createTime) {
        this.userId = userId;
        this.ip = ip;
        this.module = module;
        this.content = content;
        this.createTime = createTime;
    }
}
