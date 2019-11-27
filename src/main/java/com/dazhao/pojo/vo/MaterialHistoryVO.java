package com.dazhao.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialHistoryVO {
    
    /**
     * 没收实物名称
     */
    @ApiParam(name = "materialName", value = "没收实物名称", required = true)
    private String materialName;

    /**
     * 计量单位
     */
    @ApiParam(name = "materialUnit", value = "计量单位", required = true)
    private String materialUnit;

    /**
     * 型号
     */
    @ApiParam(name = "materialType", value = "型号")
    private String materialType;

    /**
     * 仓位编号
     */
    @ApiParam(name = "locationNumber", value = "仓位编号", required = true)
    private String locationNumber;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ApiParam(name = "createTime", value = "操作时间", hidden = true)
    private Date createTime;


    /**
     * 数量
     */
    @ApiParam(name = "materialCount", value = "数量", hidden = true)
    private Integer materialCount;


    /**
     * 物资编号
     */
    @ApiParam(name = "materialNumber", value = "物资编号", hidden = true)
    private String materialNumber;

    /**
     * 操作类型(1：入库、2：借阅、3：销毁、4：移交、5：归还)
     */
    @ApiParam(name = "operationType", value = "操作类型(1：入库、2：借阅、3：销毁、4：移交、5：归还)", hidden = true)
    private Integer operationType;

    /**
     * 物资备注
     */
    @ApiParam(name = "materialComment", value = "物资备注", hidden = true)
    private String materialComment;

    /**
     * 分类id
     */
    private Integer sortId;

    /**
     * 分类名称
     */
    private String sortName;
}
