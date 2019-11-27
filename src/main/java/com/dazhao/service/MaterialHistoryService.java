package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.pojo.dao.MaterialHistory;
import com.dazhao.pojo.vo.MaterialHistoryInformationVO;
import com.dazhao.pojo.vo.MaterialHistoryVO;
import java.util.List;

public interface MaterialHistoryService {

    /**
     * 获取物资分组详情 （借阅，移交，销毁）
     *
     * @param voucherNumber 凭证单号
     * @param operationType 操作类型(1：入库、2：借阅、3：移交、4：销毁、5：归还)
     * @return 物资分组列表
     */
    List<MaterialHistoryVO> listMaterialGroup(Page page, String voucherNumber, Integer operationType);


    /**
     * 获取物资明细列表
     *
     * @param page 分页对象
     * @param voucherNumber 凭证单号
     * @param materialName 没收实物名称
     * @param materialUnit 计量单位
     * @param materialType 型号
     * @param locationNumber 仓位编号
     * @param operationType 操作类型 2：借阅、3：移交、4：销毁
     * @param sortName 分类名称
     * @return 物资明细
     */
    List<MaterialHistoryVO> listMaterialDetails(Page page, String voucherNumber, String materialName, String materialUnit, String materialType,
            String locationNumber, String materialComment, Integer operationType, String sortName);

    /**
     * 查询一个物资追溯历史
     *
     * @param materialId 物资id
     * @return 物资历史集合
     */
    List<MaterialHistoryInformationVO> queryMaterialHistoryById(Integer materialId);
}
