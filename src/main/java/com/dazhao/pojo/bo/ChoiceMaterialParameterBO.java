package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@ApiModel(description = "销毁物资选择条件查询参数")
@Data
public class ChoiceMaterialParameterBO {

    /**
     * 物资编号
     */
    @ApiModelProperty(notes = "物资编号")
    @ApiParam(name = "materialNumber", value = "物资编号", required = false)
    private String materialNumber;

    /**
     * 没收实物名称
     */
    @ApiModelProperty(notes = "没收实物名称")
    @ApiParam(name = "materialName", value = "没收实物名称", required = false)
    private String materialName;

    /**
     * 凭证单号
     */
    @ApiModelProperty(notes = "凭证单号")
    @ApiParam(name = "voucherNumber", value = "凭证单号", required = false)
    private String voucherNumber;

    /**
     * 入库源id
     */
    @ApiModelProperty(notes = "入库源id")
    @ApiParam(name = "materialSourceId", value = "入库源id", required = false)
    private Integer materialSourceId;
}
