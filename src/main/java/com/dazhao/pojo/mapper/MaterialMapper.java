package com.dazhao.pojo.mapper;

import com.dazhao.pojo.bo.ChoiceMaterialParameterBO;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.bo.MaterialGroupQueryConditionBO;
import com.dazhao.pojo.bo.MaterialProblemBO;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.BillAndMaterialDetailsVO;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.CheckMaterialVO;
import com.dazhao.pojo.vo.ChoiceBillVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import com.dazhao.pojo.vo.StatisticsVo;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MaterialMapper extends Mapper<Material>, MySqlMapper<Material> {


    /**
     * 批量修改物资状态
     *
     * @param materialNumbers 物资编号
     * @param materialStatus 物资状态
     */
    void updateMaterialByMaterialNumbers(@Param("materialNumbers") List<String> materialNumbers, @Param("materialStatus") Integer materialStatus);

    /**
     * 查询物资id集合
     *
     * @param materialNumbers 物资编号集合
     * @return 物资id集合
     */
    List<Integer> listMaterialIdByMaterialNumbers(@Param("materialNumbers") List<String> materialNumbers);

    /**
     * 根据物资编码获取单个物资信息
     *
     * @param materialNumber 物资编码
     * @return 物资信息
     */
    Material getMaterialByMaterialNumber(@Param("materialNumber") String materialNumber);


    /**
     * 根据条件查询物资分组下的物资详情
     *
     * @param materialGroupParameterBO 查询条件
     * @return 物资集合
     */

    List<Material> queryInboundMaterialGroupDetailByConditon(MaterialGroupParameterBO materialGroupParameterBO);

    List<Material> queryMaterialGroupDetailByConditon(MaterialGroupParameterBO materialGroupParameterBO);

    /**
     * 批量修改物资状态
     *
     * @param materialIdList 物资id
     */
    void updateMaterialStatusByMaterialId(@Param("materialIdList") List<Integer> materialIdList,
            @Param("newMaterialStatus") Integer newMaterialStatus,
            @Param("oldMaterialStatus") Integer oldMaterialStatus, @Param("updateTime") Date updateTime);

    /**
     * 查询满足销毁的的单据
     *
     * @param choiceMaterialParameterBO 条件查询对象
     * @return 满足销毁的单据集合
     */
    List<ChoiceBillVO> queryCanDestoryBill(ChoiceMaterialParameterBO choiceMaterialParameterBO);


    /**
     * 根据单据id数组查询物资信息
     *
     * @param billIds 单据id数组
     * @return 物资信息对象
     */
    List<MaterialGroupVO> queryDestoryMaterialGroupByBillId(@Param("billIds") Integer[] billIds);

    /**
     * 根据单据数组查询出所有物资
     *
     * @param billIds 入库单单据id数组
     * @return 物资集合
     */
    List<Material> queryDestoryMaterial(@Param("billIds") Integer[] billIds);

    /**
     * 检查物资状态是否符合操作状态
     *
     * @param materialStatus 物资当前状态
     * @param materialIdList 物资id集合
     * @return 数量
     */
    int checkMatrialStatusIfSatisfies(@Param("materialStatus") Integer materialStatus, @Param("materialIdList") List<Integer> materialIdList);

    /**
     * 查询所有物资分组
     *
     * @param materialGroupQueryConditionBO 查询条件
     * @return 分组集合
     */
    List<MaterialGroupVO> queryMaterialGroupByCondition(MaterialGroupQueryConditionBO materialGroupQueryConditionBO);

    /**
     * 查询物资分组下的所有物资
     *
     * @param materialGroupParameterBO 查询条件
     * @return 物资信息集合
     */
    List<BillAndMaterialVO> queryMaterialGroupDetailsByCondition(MaterialGroupParameterBO materialGroupParameterBO);

    /**
     * 根据物资id查询物资跟单据相关详情
     *
     * @param materialId 物资id
     * @return 物资和单据相关对象
     */
    BillAndMaterialDetailsVO queryMaterialAndBillDetialsByMaterialId(Integer materialId);

    /**
     * 查询在库物资分组
     *
     * @param materialName 物资名称
     * @param locationId 仓位id
     * @return 物资分组集合
     */
    List<MaterialGroupVO> queryExistMaterialGroup(@Param("materialName") String materialName, @Param("locationId") Integer locationId);

    /**
     * 查询在库物资分组详情
     *
     * @param materialGroupParameterBO 物资查询条件
     */
    List<CheckMaterialVO> queryExistMaterialGroupDetail(MaterialGroupParameterBO materialGroupParameterBO);

    /**
     * 修改物资的状态和备注信息
     *
     * @param materialProblemBOList 物资问题修改信息
     */
    void updateMaterialStatusAndComment(@Param("list") List<MaterialProblemBO> materialProblemBOList);

    /**
     * 批量跟新物资仓位号
     */
    void updateMaterialLocation(@Param("locationId") Integer locationId, @Param("materialList") List<Material> materailList);

    /**
     * 查询物资根据入库源分组的占比数据
     */
    List<StatisticsVo> listSourceProportion();

    /**
     * 根据入库源渠id 查询物资类别占比
     *
     * @param sourceId 入库源id
     */
    List<StatisticsVo> listSortProportion(@Param("sourceId") Integer sourceId);

    /**
     * 根据时间单位天或月统计入库数量
     *
     * @param timeUnit 时间单位类型
     */
    List<StatisticsVo> inboundMaterialStatistics(@Param("timeUnit") Integer timeUnit);

    /**
     * 查询到期物资数据
     *
     * @return 可销毁物资总数
     */
    int queryMaterialExpireCount();

    /**
     * 根据物资编号更新物资RFID打印状态
     *
     * @param materialNumber 物资编号
     * @return 是否成功
     */
    int updateMaterialPrintStatus(@Param("materialNumber") String materialNumber);


}
