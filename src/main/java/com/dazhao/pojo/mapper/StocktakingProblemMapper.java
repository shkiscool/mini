package com.dazhao.pojo.mapper;

import com.dazhao.pojo.bo.MaterialProblemBO;
import com.dazhao.pojo.dao.StocktakingProblem;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface StocktakingProblemMapper extends Mapper<StocktakingProblem>, MySqlMapper<StocktakingProblem> {

    /**
     * 批量插入检查问题物资信息
     *
     * @param id 检查id
     * @param materialProblemBOList 问题物资信息
     */
    void insertStocktakingMaterialProblem(@Param(value = "id") Integer id, @Param(value = "list") List<MaterialProblemBO> materialProblemBOList);
}
