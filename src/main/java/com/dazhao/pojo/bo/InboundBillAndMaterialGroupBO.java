package com.dazhao.pojo.bo;

import com.dazhao.pojo.dao.Material;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Api(description = "入库单信息对象")
@Data
public class InboundBillAndMaterialGroupBO {

    /**
     * 入库源id
     */
    @NotNull(message = "入库源不能空")
    @ApiModelProperty(notes = "入库源id")
    private Integer materialSourceId;

    /**
     * 凭证照片路径
     */

    @ApiModelProperty(notes = "凭证照片路径")
    private String voucherPicturePath;

    /**
     * 案件编号（入库时有值）
     */
    @NotBlank(message = "案件编号不能为空")
    @ApiModelProperty(notes = "案件编号")
    private String caseNumber;

    /**
     * 罚没对象名称（入库时有值）
     */
    @NotBlank(message = "罚没对象名称不能为空")
    @ApiModelProperty(notes = "罚没对象名称")
    private String confiscateName;

    /**
     * 地址（入库时有值）
     */
    @NotBlank(message = "地址不能为空")
    @ApiModelProperty(notes = "地址")
    private String address;

    /**
     * 送交人名称（入库时有值）
     */
    @ApiModelProperty(notes = "送交人名称")
    @NotBlank(message = "送交人名称不能为空")
    private String consignerName;

    /**
     * 送交单位（入库时有值）
     */
    @NotBlank(message = "送交单位不能为空")
    @ApiModelProperty(notes = "送交单位")
    private String consignerDepartment;

    /**
     * 接收人名称
     */
    @ApiModelProperty(notes = "接收人名称")
    @NotBlank(message = "接收人名称不能为空")
    private String recipientName;


    /**
     * 罚没日期日期（入库时有值）
     */
    @ApiModelProperty(notes = "罚没日期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "接收人名称不能为空")
    private Date confiscateDate;

    /**
     * 案由（入库时有值）
     */
    @ApiModelProperty(notes = "案由")
    @NotBlank(message = "案由不能为空")
    private String reason;

    /**
     * 处罚条款（入库时有值）
     */
    @ApiModelProperty(notes = "处罚条款")
    @NotBlank(message = "案由不能为空")
    private String clause;

    @ApiModelProperty(notes = "物资分组")
    @NotNull(message = "物资明细不能为空")
    List<Material> materialGroup;
}
