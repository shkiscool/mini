package com.dazhao.pojo.vo;

import lombok.Data;

@Data
public class CheckMaterialVO {

    /**
     * 物资编号
     */
    private String materialNumber;
    /**
     * 物资id
     */
    private Integer materialId;

    /**
     * 当前状态状态（ 1：在库、2：借阅、3：移交、4：销毁、5：冻结）
     */
    private Integer materialStatus;

    /**
     * 入库源名称
     */
    private String sourceName;
    /**
     * 入库时间
     */
    private String createTime;
    /**
     * 到期时间
     */
    private String expireTime;
    /**
     * 超期天数
     */
    private Integer overDay;
    /**
     * 是否扫描 前端使用字段
     */
    private Integer isScan = 0;
}
