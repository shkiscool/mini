package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@ApiModel(description = "盘点存在问题的物资提交信息")
public class MaterialProblemBO {

    /**
     * 物资id
     */
    @ApiParam(name = "materialId", value = "物资id", required = true)
    @NotNull(message = "物资单据不能为空")
    private Integer materialId;
    /**
     * 问题备注
     */
    @ApiParam(name = "problemDescribe", value = "问题备注", required = true)
    @NotNull(message = "问题备注不能为空")
    private String problemDescribe;
    /**
     * 物资状态
     */
    @ApiParam(name = "materialStatus", value = "物资状态", required = true)
    @NotNull(message = "物资状态")
    private Integer materialStatus;
}
