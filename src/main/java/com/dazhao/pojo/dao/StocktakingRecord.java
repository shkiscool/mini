package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "stocktaking_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "单据")
public class StocktakingRecord {

    /**
     * 盘点记录id
     */
    @ApiModelProperty(notes = "盘点记录id")
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;
    /**
     * 盘点人名称
     */
    @ApiModelProperty(notes = "盘点人名称")
    @Column(name = "checker_name")
    private String checkerName;
    /**
     * 监督人员名称
     */
    @ApiModelProperty(notes = "监督人员名称")
    @Column(name = "superintendent_name")
    private String superintendentName;
    /**
     * 创建时间
     */
    @ApiModelProperty(notes = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;
    /**
     * 图片路径
     */
    @ApiModelProperty(notes = "图片路径")
    @Column(name = "picture_url")
    private String pictureUrl;
    /**
     * 逻辑删除
     */
    @ApiModelProperty(notes = "逻辑删除")
    @Column(name = "deleted")
    private Integer deleted;

    /**
     * 问题物资
     */
    @Transient
    List<Material> problemMaterials;

    /**
     * 问题物资 数量
     */
    @Transient
    private Integer problemCount;

    /**
     * 数字签名
     */
    @ApiModelProperty(notes = "数字签名")
    @Column(name = "signature")
    private String signature;

    /**
     * 数字签名标志
     */
    @ApiModelProperty(notes = "数字签名标志")
    @Column(name = "signature_flag")
    private Integer signatureFlag;

    /**
     * 数字签名图片地址
     */
    @ApiModelProperty(notes = "数字签名图片地址")
    @Column(name = "signature_picture_path")
    private String signaturePicturePath;
}
