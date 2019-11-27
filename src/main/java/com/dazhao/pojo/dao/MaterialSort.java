package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "material_sort")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "物资分类")
public class MaterialSort {

    /**
     * 分类ID
     */
    @ApiModelProperty(notes = "分类ID", hidden = true)
    @Id
    private Integer id;

    /**
     * 分类名称
     */
    @ApiModelProperty(notes = "分类名称")
    @Column(name = "sort_name")
    @NotBlank(message = "分类名称不能为空")
    private String sortName;

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
