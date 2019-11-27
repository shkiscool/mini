package com.dazhao.external.law;

import lombok.Data;

/**
 * 法度罚没对象
 */
@Data
public class ForfeitureGoodsAddModel {

    /**
     * 案由
     */
    private String actionCause;

    /**
     * 办案人员名称
     */
    private String caseHandlerName;

    /**
     * 案件编号
     */
    private String caseNumber;

    /**
     * 没收实物名称
     */
    private String confiscationName;

    /**
     * 罚没日期
     */
    private String forfeitureDate;

    /**
     * 罚没对象名称
     */
    private String forfeitureObjName;

    /**
     * 入库源
     */
    private String inWarehouseSource;

    /**
     * 计量单位
     */
    private String measurementUnit;

    /**
     * 型号
     */
    private String model;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 接收人名称
     */
    private String receiverName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 仓位ID
     */
    private String warehousePositionId;

}
