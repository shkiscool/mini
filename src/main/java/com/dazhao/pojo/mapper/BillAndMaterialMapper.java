package com.dazhao.pojo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BillAndMaterialMapper {

    /**
     * 添加单据和物资的关联信息
     */
    void insertBillAndMaterials(@Param(value = "billId") int billId, @Param(value = "list") List<Integer> materailIdList);
}
