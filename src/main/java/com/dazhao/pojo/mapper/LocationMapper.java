package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.dao.Material;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface LocationMapper extends Mapper<Location>, MySqlMapper<Location> {

    /**
     * 查询所有仓位
     */
    List<Location> queryLocationList();

    /**
     * 根据仓位id查询该仓位下统计相同物资分组信息。
     *
     * @param locationId 仓位id
     * @return 物资分组统计后的对象集合
     */
    List<String> queryLocationMaterialGroupByid(Integer locationId);

    /**
     * 逻辑删除仓位
     */
    void deleteLocationByid(Integer locationId);

    /**
     * 该仓位号是否存在使用
     *
     * @param locationId 仓位id
     */
    int isExistUserLocationByid(Integer locationId);

    /**
     * 根据仓位号判断是否存
     *
     * @param locationNumber 仓位号
     */
    int isExistLocationByLocationNumber(String locationNumber);

    /**
     * 查询仓位数量
     *
     * @return 仓位信息集合
     */
    List<Location> queryLocationStock();

    /**
     * 修改仓位是否可存放状态
     *
     * @param locationId 仓位id
     * @param fillFlag 状态
     */
    void updateLocationFillFlag(@Param("locationId") Integer locationId, @Param("fillFlag") Integer fillFlag);

    /**
     * 查询默认推荐可存放的仓位
     * @return 单个仓位对象
     */
    Location queryDefaultLocation();

}
