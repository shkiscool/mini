package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.pojo.bo.ChoiceMaterialParameterBO;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.bo.MaterialGroupQueryConditionBO;
import com.dazhao.pojo.bo.UpdateLocationBO;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.BillAndMaterialDetailsVO;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.ChoiceBillVO;
import com.dazhao.pojo.vo.DestoryMaterialGroupAndMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import java.util.List;

public interface MaterialService {


    /**
     * 根据物资编码获取单个物资信息
     *
     * @param materialNumber 物资编码
     * @return 物资信息
     */
    Material getMaterialByMaterialNumber(String materialNumber);




    /**
     * 根据条件查询物资分组下的物资详情
     *
     * @param materialGroupParameterBO 查询条件
     * @return 物资集合
     */
    List<Material> queryInboundMaterialGroupDetailByConditon(Page page, MaterialGroupParameterBO materialGroupParameterBO);

    /**
     * 查询满足销毁的的单据
     *
     * @param page 分页对象
     * @param choiceMaterialParameterBO 条件查询对象
     * @return 满足销毁的单据集合
     */
    List<ChoiceBillVO> queryCanDestoryBill(Page page, ChoiceMaterialParameterBO choiceMaterialParameterBO);

    /**
     * 根据单据id数组查询物资信息
     *
     * @param billIds 单据id数组
     * @return 物资信息对象
     */
    DestoryMaterialGroupAndMaterialVO queryDestoryMaterialByBillId(Integer[] billIds);


    /**
     * 检查物资是否都符合查询状态并统计数量
     *
     * @param materialStatus 物资状态
     * @param materialIdList 物资id集合
     * @return 总数
     */
    int checkMatrialStatusIfSatisfies(Integer materialStatus, List<Integer> materialIdList);


    /**
     * 查询所有物资分组
     *
     * @param page 分页对象
     * @param materialGroupQueryConditionBO 查询条件
     * @return 分组集合
     */
    List<MaterialGroupVO> queryMaterialGroupByCondition(Page page, MaterialGroupQueryConditionBO materialGroupQueryConditionBO);

    /**
     * 查询物资分组下的所有物资
     *
     * @param page 分页对象
     * @param materialGroupParameterBO 查询条件
     * @return 物资信息集合
     */
    List<BillAndMaterialVO> queryMaterialGroupDetailsByCondition(Page page, MaterialGroupParameterBO materialGroupParameterBO);

    /**
     * 根据物资id查询物资跟单据相关详情
     *
     * @param materialId 物资id
     * @return 物资和单据相关对象
     */
    BillAndMaterialDetailsVO queryMaterialAndBillDetialsByMaterialId(Integer materialId);

    /**
     * 修改仓位
     *
     * @param updateLocationBO 要修改的物资集合
     * @param operationNameId 操作人id
     * @param ipAddress 操作人id
     */
    void updateMaterialLocation(UpdateLocationBO updateLocationBO, Integer operationNameId, String ipAddress);

    /**
     * Excel导出查询物资分组
     *
     * @param materialGroupQueryConditionBO 查询条件
     * @return 分组集合
     */
    List<MaterialGroupVO> queryMaterialGroupByConditionExcel(MaterialGroupQueryConditionBO materialGroupQueryConditionBO);

    /**
     * 根据物资编号更新物资RFID打印状态
     * @param materialNumber 物资编号
     * @return 是否成功
     */
    boolean updateMaterialPrintStatus(String materialNumber);
}
