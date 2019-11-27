package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.pojo.bo.CheckBO;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.dao.StocktakingRecord;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.CheckMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import java.util.List;

public interface StocktakingService {

    /**
     * 查询盘点记录
     *
     * @param page 分页对象
     * @param checkName 检查人
     * @param beginTime 开始时间
     * @param endTime 结束时间
     */
    List<StocktakingRecord> queryCheckRecords(Page page, String checkName, String beginTime, String endTime);

    /**
     * 查询在库物资分组
     *
     * @param page 分页对象
     * @param materialName 物资名称
     * @param locationId 仓位id
     * @param type 查询所有还是查询分页
     * @return 物资分组集合
     */
    List<MaterialGroupVO> queryExistMaterialGroup(Page page, String materialName, Integer locationId, Integer type);

    /**
     * 查询在库物资分组详情
     *
     * @param page 分页对象
     * @param materialGroupParameterBO 物资查询条件
     */
    List<CheckMaterialVO> queryExistMaterialGroupDetail(Page page, MaterialGroupParameterBO materialGroupParameterBO, Integer type);

    /**
     * 添加盘点记录和盘点问题
     *
     * @param checkBO 盘点提交信息
     */
    void insertStocktakingRecordAndMaterialProblem(CheckBO checkBO, Integer userId, String ipAddress);

    /**
     * 根据盘点记录id查询盘点详情
     *
     * @param id 记录id
     * @return 详情对象
     */
    StocktakingRecord queryCheckRecordsById(Integer id);

    /**
     * 添加盘点记录的图片地址
     *
     * @param stocktakingRecord 盘点对象
     * @param userId 操作人id
     * @param ipAddress 操作ip
     */
    CommonResult updateStocktakingRecordPictureUrl(StocktakingRecord stocktakingRecord, Integer userId, String ipAddress);

    /**
     * 删除盘点记录的图片地址
     *
     * @param stocktakingRecord 盘点对象
     * @param userId 操作人id
     * @param ipAddress 操作ip
     */
    boolean deleteStocktakingRecordPictureUrl(StocktakingRecord stocktakingRecord, Integer userId, String ipAddress);

    /**
     * 更新盘点单据签名信息
     *
     * @param signatureHash 签名哈希值
     * @param urlPath 签名图片地址
     * @param billId 盘点单据id
     */
    void uploadStocktakingSignature(String signatureHash, String urlPath, Integer billId);
}
