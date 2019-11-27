package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 检查结果对象
 */
@Data
@ApiModel(description = "检查结果提交对象")
public class CheckBO {

    /**
     * 检查人名称
     */
    @NotNull(message = "检查人名称不能为空")
    @ApiModelProperty(notes = "检查人名称")
    private String checkerName;
    /**
     * 监督人员名称
     */
    @NotNull(message = "监督人员名称不能为空")
    @ApiModelProperty(notes = "监督人员名称")
    private String superintendentName;
    /**
     * 图片路径
     */
    @ApiModelProperty(notes = "图片路径")
    private String pictureUrl;
    /**
     * 盘点存在问题的物资提交信息
     */
    @ApiModelProperty(notes = "盘点存在问题的物资提交信息")
    List<MaterialProblemBO> materialProblemBOList;

}
