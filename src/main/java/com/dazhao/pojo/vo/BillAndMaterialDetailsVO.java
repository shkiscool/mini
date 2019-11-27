package com.dazhao.pojo.vo;

import lombok.Data;

@Data
public class BillAndMaterialDetailsVO {

    /**
     * 入库源名称
     */
    private String sourceName;
    /**
     * 凭证单号
     */
    private String voucherNumber;

    /**
     * 单据id
     */
    private Integer billId;
    /**
     * 案件编号
     */
    private String caseNumber;
    /**
     * 罚没对象名称
     */
    private String confiscateName;
    /**
     * 地址
     */
    private String address;

    /**
     * 送交人名称
     */
    private String consignerName;
    /**
     * 送交单位
     */
    private String consignerDepartment;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 案由
     */
    private String reason;
    /**
     * 处罚条款
     */
    private String clause;
    /**
     * 没收实物名称
     */
    private String materialName;
    /**
     * 计量单位
     */
    private String materialUnit;
    /**
     * 型号
     */
    private String materialType;
    /**
     * 物资状态
     */
    private String materialStatus;
    /**
     * 备注
     */
    private String materialComment;
    /**
     * 物资数量
     */
    private Integer total;
    /**
     * 仓位编号
     */
    private String locationNumber;

    /**
     * 分类名称
     */
    private String sortName;
    /**
     * 到期时间
     */
    private String expireTime;

    /**
     * 超期天数
     */
    private Integer overDay;

    /**
     * 物资编号
     */
    private String materialNumber;

    /**
     * 接收人
     */
    private String recipientName;

    /**
     * 罚没日期
     */
    private String confiscateDate;
}
