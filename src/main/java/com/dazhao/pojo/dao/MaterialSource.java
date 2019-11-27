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
 * 入库源
 */
@Table(name = "material_source")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "入库源")
public class MaterialSource {

    /**
     * 入库源id
     */
    @ApiModelProperty(notes = "入库源id", hidden = true)
    @Id
    private Integer id;

    /**
     * 入库源名称
     */
    @ApiModelProperty(notes = "入库源名称")
    @Column(name = "source_name")
    private String sourceName;

    /**
     * 提醒时间
     */
    @ApiModelProperty(notes = "提醒时间")
    @Column(name = "keep_days")
    private Integer keepDays;

    /**
     * 创建时间
     */
    @ApiModelProperty(notes = "创建时间", hidden = true)
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(notes = "更新时间", hidden = true)
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(notes = "逻辑删除", hidden = true)
    @Column(name = "deleted")
    private Integer deleted;
}
