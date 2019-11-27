package com.dazhao.pojo.vo;

import lombok.Data;

@Data
public class BillAndMaterialVO {

    /**
     * 凭证单号
     */
    private String voucherNumber;

    /**
     * 物资编号
     */
    private String materialNumber;

    /**
     * 物资id
     */
    private Integer materialId;

    /**
     * 物资名称
     */
    private String materialName;

    /**
     * 单据id
     */
    private String billId;

    /**
     * 入库源名称
     */
    private String sourceName;

    /**
     * 到期时间
     */
    private String expireTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 数量
     */
    private Integer count;
}
