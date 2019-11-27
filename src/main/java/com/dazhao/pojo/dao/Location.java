package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 仓位
 */
@Table(name = "location")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "仓位")
public class Location {

    /**
     * 仓位ID
     */
    @ApiModelProperty(notes = "仓位ID")
    @Id
    private Integer id;

    /**
     * 仓位编号
     */
    @ApiModelProperty(notes = "仓位编号")
    @Column(name = "location_number")
    @NotBlank(message = "仓位编号不能为空")
    private String locationNumber;

    /**
     * 描述
     */
    @ApiModelProperty(notes = "描述")
    @Column(name = "location_describe")
    @NotBlank(message = "仓位描述不能为空")
    private String locationDescribe;

    /**
     * 创建时间
     */
    @ApiModelProperty(notes = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(notes = "修改时间")
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
     * 仓位是否填满 0 未填满 1 填满
     */
    @ApiModelProperty(notes = "仓位是否填满 0 未填满 1 填满")
    @Column(name = "fill_flag")
    private Integer fillFlag;
    /**
     * 仓位存量
     */
    @Transient
    private Integer stock;
}
