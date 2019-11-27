package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.dao.Material;
import java.util.List;

public interface LocationService {

    /**
     * 查询仓位集合
     *
     * @return 仓位集合
     */
    List<Location> queryLocationList(Page page);

    /**
     * 根据仓位id查询该仓位下统计相同物资分组信息。
     *
     * @param locationId 仓位id
     * @return 物资分组统计后的对象集合
     */
    List<String> queryLocationMaterialGroupByid(Integer locationId);

    /**
     * 根据仓位id判断是否存在该仓位
     *
     * @param locationId 仓位id
     * @return 是否存在
     */
    Boolean isExistLocationByid(Integer locationId);

    /**
     * 逻辑删除仓位
     *
     * @param locationId 仓位id
     * @param userId 操作id
     * @param ipAddress 操作ip
     */
    void deleteLocationByid(Integer locationId, int userId, String ipAddress);

    /**
     * 该仓位号是否存在使用
     *
     * @param locationId 仓位id
     */
    Boolean isExistUserLocationByid(Integer locationId);

    /**
     * 根据仓位号判断是否存
     *
     * @param locationNumber 仓位号
     */
    Boolean isExistLocationByLocationNumber(String locationNumber);

    /**
     * 创建仓位
     *
     * @param location 仓位对象
     * @param userId 操作id
     * @param ipAddress 操作ip
     */
    void insertLocation(Location location, int userId, String ipAddress);

    /**
     * 修改仓位是否可存放状态
     *
     * @param locationId 仓位id
     * @param fillFlag 状态
     */
    void updateLocationFillFlag(Integer locationId, Integer fillFlag);

    /**
     * 查询默认推荐可存放的仓位
     * @return 单个仓位对象
     */
    Location queryDefaultLocation();

}
