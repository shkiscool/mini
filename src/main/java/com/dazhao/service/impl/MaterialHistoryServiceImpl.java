package com.dazhao.service.impl;

import com.dazhao.common.MaterialOperationTypeEnum;
import com.dazhao.common.Page;
import com.dazhao.pojo.dao.MaterialHistory;
import com.dazhao.pojo.mapper.MaterialHistoryMapper;
import com.dazhao.pojo.vo.MaterialHistoryInformationVO;
import com.dazhao.pojo.vo.MaterialHistoryVO;
import com.dazhao.service.MaterialHistoryService;
import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialHistoryServiceImpl implements MaterialHistoryService {

    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;


    /**
     * 获取物资分组详情 （借阅，移交，销毁）
     *
     * @param voucherNumber 凭证单号
     * @param operationType 操作类型(1：入库、2：借阅、3：移交、4：销毁、5：归还)
     * @return 物资分组列表
     */
    @Override
    public List<MaterialHistoryVO> listMaterialGroup(Page page, String voucherNumber, Integer operationType) {
        return PageHelper.startPage(page).doSelectPage(() -> materialHistoryMapper.listMaterialGroup(voucherNumber, operationType));
    }

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
    @Override
    public List<MaterialHistoryVO> listMaterialDetails(Page page, String voucherNumber, String materialName, String materialUnit, String materialType,
            String locationNumber, String materialComment, Integer operationType, String sortName) {
        List<MaterialHistoryVO> materialHistoryList = new ArrayList<>();
        if (operationType.equals(MaterialOperationTypeEnum.MATERIAL_BORROW.getOperationValue())) { // 借阅物资明细
            //查询归还列表
            List<MaterialHistoryVO> materialGiveBackList = PageHelper.startPage(page).doSelectPage(() ->
                    materialHistoryMapper.listMaterialDetails(voucherNumber, materialName, materialUnit,
                            materialType, locationNumber, MaterialOperationTypeEnum.MATERIAL_REVERT.getOperationValue(), materialComment, sortName));
            // 查询借阅列表
            materialHistoryList = PageHelper.startPage(page).doSelectPage(() ->
                    materialHistoryMapper.listMaterialDetails(voucherNumber, materialName, materialUnit, materialType, locationNumber,
                            MaterialOperationTypeEnum.MATERIAL_BORROW.getOperationValue(), materialComment, sortName));
            materialHistoryList = materialHistoryList.stream()
                    .map((x) -> {
                        for (MaterialHistoryVO materialGiveBack : materialGiveBackList) {
                            if (materialGiveBack.getMaterialNumber().equals(x.getMaterialNumber())) {
                                return materialGiveBack;
                            }
                        }
                        x.setCreateTime(null);// 未归还将时间设置为null
                        return x;
                    }).collect(Collectors.toList());
        } else if (operationType.equals(MaterialOperationTypeEnum.MATERIAL_TRANSFER.getOperationValue())
                || operationType.equals(MaterialOperationTypeEnum.MATERIAL_DESTORY.getOperationValue())) { // 移交和销毁物资明细
            materialHistoryList = PageHelper.startPage(page)
                    .doSelectPage(() -> materialHistoryMapper.listMaterialDetails(voucherNumber, materialName, materialUnit,
                            materialType, locationNumber, operationType, materialComment, sortName));
        }
        return materialHistoryList;
    }

    @Override
    public List<MaterialHistoryInformationVO> queryMaterialHistoryById(Integer materialId) {
        return materialHistoryMapper.queryMaterialHistoryById(materialId);
    }

}
