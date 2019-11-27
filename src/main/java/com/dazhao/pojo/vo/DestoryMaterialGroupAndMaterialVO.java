package com.dazhao.pojo.vo;

import com.dazhao.pojo.dao.Material;
import java.util.List;
import lombok.Data;

@Data
/**
 * 物资销毁对象
 */
public class DestoryMaterialGroupAndMaterialVO {

    /**
     * 物资分组集合
     */
    List<MaterialGroupVO> materialGroupVOList;

    /**
     * 物资集合
     */
    List<Material> materialList;

    public DestoryMaterialGroupAndMaterialVO() {
    }

    public DestoryMaterialGroupAndMaterialVO(List<MaterialGroupVO> materialGroupVOList, List<Material> materialList) {
        this.materialGroupVOList = materialGroupVOList;
        this.materialList = materialList;
    }
}
