package com.dazhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel(description = "所有物资分组查询条件对象")
public class MaterialGroupQueryConditionBO {

    /**
     * 物资编号
     */
    @ApiParam(name = "materialNumber", value = "物资编号", required = false)
    private String materialNumber;
    /**
     * 没收实物名称
     */
    @ApiParam(name = "materialName", value = "没收实物名称", required = false)
    private String materialName;
    /**
     * 入库时间
     */
    @ApiParam(name = "createTime", value = "入库时间", required = false)
    private String createTime;

    /**
     * 到期开始时间
     */
    @ApiParam(name = "createTime", value = "到期开始时间", required = false)
    private String expireBeginTime;

    /**
     * 到期开始时间
     */
    @ApiParam(name = "createTime", value = "到期开始时间", required = false)
    private String expireEndTime;

    /**
     * 入库源id
     */
    @ApiParam(name = "sourceId", value = "入库源id", required = false)
    private Integer sourceId;
    /**
     * 物资状态id值
     */
    @ApiParam(name = "materialStatus", value = "当前物资状态状态（ 1：在库、2：借阅、3：移交、4：销毁、5：冻结）", required = false)
    private Integer materialStatus;


}
