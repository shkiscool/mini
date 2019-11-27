package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.pojo.dao.MaterialSource;
import java.util.List;

public interface MaterialSourceService {

    /**
     * 新增入库源
     */
    void insertSource(MaterialSource materialSource, Integer userId, String ipAddress);

    /**
     * 根据id删除入库源
     */
    boolean deleteSourceById(Integer id, Integer userId, String ipAddress);

    /**
     * 查询入库源列表
     */
    List<MaterialSource> listSources(Page page);

    /**
     * 修改入库源
     */
    void updateSource(MaterialSource materialSource);

    /**
     * 根据入库源id判断是否存在入库源
     *
     * @param soourceId 入库源id
     * @return 是否存在
     */
    boolean isExistMaterialSourceById(Integer soourceId);

    /**
     * 判断该入库源是否存在使用
     *
     * @param id 入库源id
     * @return 是否存在使用
     */
    Boolean isExistUserMaterialSource(Integer id);
}
