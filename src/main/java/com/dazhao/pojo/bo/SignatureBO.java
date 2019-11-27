package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@ApiModel(description = "签名提交对象")
public class SignatureBO {

    /**
     * 图片的base64
     */
    @NotBlank(message = "图片base64不能为空")
    @ApiModelProperty(notes = "图片的base64")
    private String imageBase64;

    /**
     * 单据id
     */
    @NotNull(message = "单据ID不能为空")
    @ApiModelProperty(notes = "单据id")
    private Integer billId;

    /**
     * type=1:单据签名；type=2:盘点签名
     */
    @NotNull(message = "类型不能为空")
    @ApiModelProperty(notes = "type=1:单据签名；type=2:盘点签名")
    private Integer type;
}
