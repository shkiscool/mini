package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.MaterialSource;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 入库源
 */
public interface MaterialSourceMapper extends Mapper<MaterialSource>,MySqlMapper<MaterialSource> {

    /**
     * 判断是否存在入库源使用
     *
     * @param sourceId 入库源id
     * @return 存在数量
     */
    int isExistUserMaterialSource(@Param("sourceId") Integer sourceId);
}
