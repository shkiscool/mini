package com.dazhao.pojo.vo;

import lombok.Data;

@Data
public class MaterialHistoryInformationVO {

    /**
     * 单据类型(1：入库、2：借阅、3：移交、4：销毁)
     */
    private Integer billType;
    /**
     * 移交人
     */
    private String handOverName;

    /**
     * 接收单位
     */
    private String receivedDepartment;

    /**
     * 操作类型(1：入库、2：借阅、3：移交、4：销毁、5：归还、6：仓位变更)
     */
    private Integer operationType;
    /**
     * 操作时间
     */
    private String createTime;

    /**
     * 备注
     */
    private String comment;

    /**
     * 操作人名称
     */
    private String operationName;
}
