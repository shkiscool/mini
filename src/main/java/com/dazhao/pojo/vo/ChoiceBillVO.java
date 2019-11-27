package com.dazhao.pojo.vo;

import lombok.Data;

@Data
public class ChoiceBillVO {

    /**
     * 单据id
     */
    private Integer id;
    /**
     * 入库单号
     */
    private String voucherNumber;
    /**
     * 案由
     */
    private String caseNumber;
    /**
     * 入库时间
     */
    private String createTime;
    /**
     * 到期时间
     */
    private String expireTime;
    /**
     * 入库源名称
     */
    private String sourceName;
    /**
     * 存放时间
     */
    private Integer keepDays;
}
