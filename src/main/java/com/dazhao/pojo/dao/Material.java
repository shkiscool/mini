package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * 物资详情
 */
@Table(name = "material")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "物资详情")
public class Material {

    /**
     * 物资ID
     */
    @ApiModelProperty(notes = "物资ID")
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;

    /**
     * 物资编号
     */
    @ApiModelProperty(notes = "物资编号")
    @Column(name = "material_number")
    private String materialNumber;

    /**
     * 当前状态状态（ 1：在库、2：借阅、3：移交、4：销毁、5：冻结）
     */
    @ApiModelProperty(notes = "当前状态状态（ 1：在库、2：借阅、3：移交、4：销毁、5：冻结）")
    @Column(name = "material_status")
    private Integer materialStatus;

    /**
     * 仓位
     */
    @ApiModelProperty(notes = "仓位")
    @Column(name = "location_id")
    private Integer locationId;

    /**
     * 没收实物名称
     */
    @ApiModelProperty(notes = "没收实物名称")
    @Column(name = "material_name")
    private String materialName;

    /**
     * 计量单位
     */
    @ApiModelProperty(notes = "计量单位")
    @Column(name = "material_unit")
    private String materialUnit;

    /**
     * 型号
     */
    @ApiModelProperty(notes = "型号")
    @Column(name = "material_type")
    private String materialType;

    /**
     * 备注（入库时有值）
     */
    @ApiModelProperty(notes = "备注（入库时有值）")
    @Column(name = "material_comment")
    private String materialComment;

    /**
     * 创建时间（入库时间）
     */
    @ApiModelProperty(notes = "创建时间（入库时间）")
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(notes = "更新时间")
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(notes = "逻辑删除")
    @Column(name = "deleted")
    private Integer deleted;

    /**
     * 是否打印RFID
     */
    @ApiModelProperty(notes = "是否打印RFID")
    @Column(name = "is_print_rfid")
    private Integer printRFID;


    @ApiModelProperty(notes = "分类id")
    @Column(name = "sort_id")
    private Integer sortId;

    /**
     * 物资数量
     */
    @Transient
    private Integer total;

    /**
     * 入库源名称
     */
    @Transient
    private String sourceName;

    /**
     * 仓位编号
     */
    @Transient
    private String locationNumber;

    /**
     * 分类名称
     */
    @Transient
    private String sortName;

    /**
     * 存放时长
     */
    @Transient
    private Integer keepDays;

    /**
     * 凭证号
     */
    @Transient
    private String voucherNumber;

    /**
     * 存放时间
     */
    @Transient
    private String storeTime;

    /**
     * 是否扫描
     */
    @Transient
    private String isScan;
}
