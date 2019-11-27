package com.dazhao.pojo.bo;

import com.dazhao.pojo.dao.Material;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@ApiModel(description = "修改仓位信息对象")
public class UpdateLocationBO {

    /**
     * 仓位id
     */
    @ApiModelProperty(notes = "变更后的仓位id")
    @NotNull(message = "仓位id不能为空")
    private Integer locationId;
    /**
     * 修改仓位的物资集合
     */
    @ApiModelProperty(notes = "要修改的物资信息")
    @NotNull(message = "仓位集合不能为空")
    private List<Material> materailList;
}
