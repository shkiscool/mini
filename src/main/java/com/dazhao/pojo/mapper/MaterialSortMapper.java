package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.MaterialSort;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MaterialSortMapper extends Mapper<MaterialSort>, MySqlMapper<MaterialSort> {

    /**
     * 统计该物资相同分类有几条
     *
     * @param materialSortId 分类id
     * @return 使用该分类的总物资条数
     */
    int isUseMaterialSort(Integer materialSortId);
}
