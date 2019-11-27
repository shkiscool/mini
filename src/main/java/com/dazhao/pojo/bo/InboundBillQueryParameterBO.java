package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import javax.persistence.Column;
import lombok.Data;

@ApiModel(description = "入库单条件查询")
@Data
public class InboundBillQueryParameterBO {

    /**
     * 凭证单号
     */
    @ApiParam(name = "voucherNumber", value = "凭证单号", required = false)
    private String voucherNumber;

    /**
     * 案件编号（入库时有值）
     */
    @ApiParam(name = "caseNumber", value = "案件编号", required = false)
    private String caseNumber;

    /**
     * 送交人名称（入库时有值）
     */
    @ApiParam(name = "consignerName", value = "送交人名称", required = false)
    private String consignerName;

    /**
     * 送交单位（入库时有值）
     */
    @ApiParam(name = "consignerName", value = "送交单位（入库时有值）", required = false)
    private String consignerDepartment;
    /**
     * 入库开始时间
     */
    @ApiModelProperty(notes = "入库时间段开始时间")
    @ApiParam(name = "beginTime", value = "入库开始时间", required = false)
    private String beginTime;

    /**
     * 入库时间段结束时间
     */
    @ApiModelProperty(notes = "入库时间段结束时间")
    @ApiParam(name = "endTime", value = "入库时间段结束时间", required = false)
    private String endTime;
    /**
     * 入库源id
     */
    @ApiParam(name = "materialSourceId", value = "入库源id", required = false)
    private Integer materialSourceId;
}
