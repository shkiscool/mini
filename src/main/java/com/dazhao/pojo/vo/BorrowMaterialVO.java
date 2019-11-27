package com.dazhao.pojo.vo;

import lombok.Data;

@Data
public class BorrowMaterialVO {

    /**
     * 物资id
     */
    private Integer materialId;

    /**
     * 物资编号
     */
    private String materialNumber;

    /**
     * 入库源
     */
    private String sourceName;

    /**
     * 入库时间
     */
    private String createTime;

    /**
     * 存放时间
     */
    private Integer keepDays;

    /**
     * 是否归还 1已完成 0待归还 默认为0
     */
    private Integer isReturn = 0;
}
