package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;


@Data
@ApiModel(description = "创建销毁单据对象")
public class OutBillBO {

    @ApiModelProperty(notes = "单据类型（2：借阅，3：移交. 4:销毁）")
    @Range(min = 2, max = 4, message = "单据类型参数错误")
    @NotNull(message = "单据类型不能为空")
    private Integer billType;

    @ApiModelProperty(notes = "移交人")
    @NotBlank(message = "移交人不能为空")
    private String handOverName;

    @ApiModelProperty(notes = "接收单位")
    @NotBlank(message = "接收单位不能为空")
    private String receivedDepartment;

    @ApiModelProperty(notes = "物资id集合")
    @NotNull(message = "接收单位不能为空")
    private List<Integer> materialIdList;

}
