package com.dazhao.service;

import com.dazhao.pojo.bo.DateStatisticsMaterialBO;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.StatisticsVo;
import java.util.List;
import java.util.Map;

public interface StatisticsService {

    /**
     * 查询物资根据入库源分组的占比数据
     */
    List<StatisticsVo> querySourceProportion();

    /**
     * 根据入库源渠id 查询物资类别占比
     *
     * @param sourceId 入库源id
     */
    List<StatisticsVo> querySortProportion(Integer sourceId);


    /**
     * 统计物资进出数据.
     *
     * @param timeUnit 时间单位
     */
    Map<String, List<DateStatisticsMaterialBO>> materialFluctuateTrend(Integer timeUnit);

    /**
     * 查询仓位数量
     *
     * @return 仓位信息集合
     */
    List<Location> queryLocationStock();

    /**
     * 根据提醒条件查询到期单据信息
     *
     * @return 到期单据集合
     */
    List<BillAndMaterialVO> queryBillExpire();

    /**
     * 查询到期物资数据
     *
     * @return 可销毁物资总数
     */
    int queryMaterialExpireCount();

    /**
     * 查询最后入库、最近出库、近期盘点数据
     */
    Map<String, Object> queryRecentOperation();

}
