package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物资追溯
 */
@Table(name = "material_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "物资追溯")
public class MaterialHistory {

    /**
     * 追溯ID
     */
    @ApiModelProperty(notes = "追溯ID")
    @Id
    private Integer id;

    /**
     * 单据ID
     */
    @ApiModelProperty(notes = "单据ID")
    @Column(name = "bill_id")
    private Integer billId;

    /**
     * 物资id
     */
    @ApiModelProperty(notes = "物资id")
    @Column(name = "material_id")
    private Integer materialId;

    /**
     * 操作类型(1：入库、2：借阅、3：销毁、4：移交、5：归还)
     */
    @ApiModelProperty(notes = "操作类型(1：入库、2：借阅、3：移交、4：销毁、5：归还)")
    @Column(name = "operation_type")
    private Integer operationType;

    /**
     * 操作时间（借阅、销毁、移交、归还时间）
     */
    @ApiModelProperty(notes = "操作时间（借阅、销毁、移交、归还时间）")
    @Column(name = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(notes = "逻辑删除")
    @Column(name = "deleted")
    private Integer deleted;

    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注")
    @Column(name = "comment")
    private String comment;
    /**
     * 操作人id
     */
    @ApiModelProperty(notes = "操作人id")
    @Column(name = "opration_name_id")
    private Integer oprationNameId;
}
