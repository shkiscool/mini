package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 盘点问题对象
 */
@Table(name = "stocktaking_problem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "盘点问题记录")
public class StocktakingProblem {

    /**
     * 检查问题id
     */
    @ApiModelProperty(notes = "检查问题id")
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;
    /**
     * 盘点id
     */
    @ApiModelProperty(notes = "盘点id")
    @Column(name = "check_records_id")
    private Integer checkRecordsId;
    /**
     * 物资id
     */
    @ApiModelProperty(notes = "物资id")
    @Column(name = "material_id")
    private Integer materialId;
    /**
     * 物资状态
     */
    @ApiModelProperty(notes = "物资状态")
    @Column(name = "material_status")
    private Integer materialStatus;
    /**
     * 问题备注
     */
    @ApiModelProperty(notes = "问题备注")
    @Column(name = "problem_describe")
    private String problemDescribe;
    /**
     * 创建时间
     */
    @ApiModelProperty(notes = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

}
