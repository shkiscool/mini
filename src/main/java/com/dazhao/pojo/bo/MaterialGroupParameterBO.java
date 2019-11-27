package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.Data;

@ApiModel(description = "分组物资查询分组下物资详情查询对象")
@Data
public class MaterialGroupParameterBO {

    @ApiModelProperty(notes = "单据id")
    @ApiParam(name = "billId", value = "单据id", required = false)
    private Integer billId;

    @ApiModelProperty(notes = "分类id")
    @ApiParam(name = "sortId", value = "分类id", required = false)
    private Integer sortId;
    /**
     * 没收实物名称
     */
    @ApiModelProperty(notes = "没收实物名称")
    @ApiParam(name = "materialName", value = "没收实物名称", required = false)
    @NotNull(message = "实物名称不能为null")
    private String materialName;
    /**
     * 计量单位
     */
    @ApiModelProperty(notes = "计量单位")
    @ApiParam(name = "materialUnit", value = "计量单位", required = false)
    private String materialUnit;
    /**
     * 型号
     */
    @ApiModelProperty(notes = "型号")
    @ApiParam(name = "materialType", value = "型号", required = false)
    private String materialType;
    /**
     * 备注（入库时有值）
     */
    @ApiModelProperty(notes = "备注")
    @ApiParam(name = "materialComment", value = "备注", required = false)
    private String materialComment;
    /**
     * 仓位
     */
    @ApiModelProperty(notes = "仓位id")
    @ApiParam(name = "locationId", value = "仓位id", required = false)
    private Integer locationId;

    /**
     * 创建时间(所有物资分组查询对应分组详细物资时有值)
     */
    @ApiModelProperty(notes = "入库时间")
    @ApiParam(name = "createTime", value = "入库时间", required = false)
    private String createTime;
    /**
     * 物资状态(所有物资分组查询对应分组详细物资时有值)
     */
    @ApiModelProperty(notes = "物资状态")
    @ApiParam(name = "materialStatus", value = "物资状态", required = false)
    private String materialStatus;
    /**
     * 物资入库源id(所有物资分组查询对应分组详细物资时有值)
     */
    @ApiModelProperty(notes = "物资入库源id")
    @ApiParam(name = "materialSourceId", value = "物资入库源id", required = false)
    private Integer materialSourceId;
}
