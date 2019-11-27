package com.dazhao.pojo.vo;

import lombok.Data;

/**
 * 单据下的物资分组返回对象
 */
@Data
public class MaterialGroupVO {

    /**
     * 仓位id
     */
    private Integer locationId;
    /**
     * 仓位号
     */
    private String locationNumber;
    /**
     * 物资名称
     */
    private String materialName;

    /**
     * 物资计量单位
     */
    private String materialUnit;

    /**
     * 物资类型
     */
    private String materialType;
    /**
     * 物资备注
     */
    private String materialComment;
    /**
     * 物资所属单据id
     */
    private Integer billId;
    /**
     * 相同物资分组总数
     */
    private Integer total;
    /**
     * 分类
     */
    private String sortName;
    /**
     * 分类id
     */
    private Integer sortId;

    /**
     * 扫描数量默认为零
     */
    private Integer scanNumber = 0;

    /**
     * 入库源名称
     */
    private String sourceName;

    /**
     * 入库源id
     */
    private String materialSourceId;


    /**
     * 当前状态状态（ 1：在库、2：借阅、3：移交、4：销毁、5：冻结）
     */
    private Integer materialStatus;

    /**
     * 物资状态名称
     */
    private String materialStatusName;

    /**
     * 创建时间（入库时间）
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
     * 是否全部归还通道门扫描时前端分组的状态
     */
    private Integer isReturn = 0;
}
