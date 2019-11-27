package com.dazhao.service;

import com.dazhao.pojo.dao.MaterialSort;
import java.util.List;


public interface MaterialSortService {

    /**
     * 查询所有物资分类
     */
    List<MaterialSort> queryMaterialSortList();


    /**
     * 添加物资分类信息
     *
     * @param materialSort 添加信息对象
     * @param userId 用户id
     * @param ipAddress 用户ip
     */
    void addMaterialSort(MaterialSort materialSort, Integer userId, String ipAddress);

    /**
     * 逻辑删除物资分类信息
     *
     * @param userId 用户id
     * @param ipAddress 用户ip
     * @param materialSortId 物资id
     */
    void deleteMaterialSortById(Integer materialSortId, Integer userId, String ipAddress);

    /**
     * 判断物资分类是否已经有在用了
     *
     * @param materialSortId 分类id
     * @return 是否存在所有
     */
    boolean isUseMaterialSort(Integer materialSortId);

    /**
     * 判断物资分类id是否存在
     *
     * @param sortId 物资分类id
     * @return 是否存在
     */
    boolean isExistMaterialSortById(Integer sortId);

    /**
     * 根据分类名称查询是否存在该分类
     *
     * @param sortName 分类名称
     * @return 是否存在
     */
    boolean isExistMaterialSortByName(String sortName);
}
