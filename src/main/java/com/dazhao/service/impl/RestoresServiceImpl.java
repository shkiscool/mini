package com.dazhao.service.impl;

import com.dazhao.common.BillEnum;
import com.dazhao.common.MaterialOperationTypeEnum;
import com.dazhao.common.MaterialStatuEnum;
import com.dazhao.common.OperationLogEnum;
import com.dazhao.common.Page;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.mapper.BillMapper;
import com.dazhao.pojo.mapper.MaterialHistoryMapper;
import com.dazhao.pojo.mapper.MaterialMapper;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.pojo.vo.BorrowMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupAndMateialsVO;
import com.dazhao.service.RestoresService;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestoresServiceImpl implements RestoresService {

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public List<Bill> queryBillByBorrowStatus(Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return billMapper.queryBillByBorrowStatus();
    }

    @Override
    public List<MaterialGroupAndMateialsVO> queryBorrowBillMaterialGroupInformation(Integer billId) {
        List<MaterialGroupAndMateialsVO> materialGroupAndMateialsVOList = billMapper.queryBorrowBillMaterialGroupInformation(billId);
        for (MaterialGroupAndMateialsVO materialGroupAndMateialsVO : materialGroupAndMateialsVOList) {
            MaterialGroupParameterBO materialGroupParameterBO = new MaterialGroupParameterBO();
            materialGroupParameterBO.setBillId(billId);
            materialGroupParameterBO.setLocationId(materialGroupAndMateialsVO.getLocationId());
            materialGroupParameterBO.setMaterialComment(materialGroupAndMateialsVO.getMaterialComment());
            materialGroupParameterBO.setMaterialName(materialGroupAndMateialsVO.getMaterialName());
            materialGroupParameterBO.setMaterialType(materialGroupAndMateialsVO.getMaterialType());
            materialGroupParameterBO.setSortId(materialGroupAndMateialsVO.getSortId());
            materialGroupParameterBO.setMaterialUnit(materialGroupAndMateialsVO.getMaterialUnit());
            List<BorrowMaterialVO> borrowMaterialVOList = materialHistoryMapper.queryBorrowMaterialGroupDetailByConditon(materialGroupParameterBO);
            materialGroupAndMateialsVO.setMaterials(borrowMaterialVOList);
        }
        return materialGroupAndMateialsVOList;
    }


    @Override
    public int borrowMaterialExists(Integer billId, List<Integer> materialList) {
        return materialHistoryMapper.borrowMaterialExists(billId, materialList);
    }

    @Override
    @Transactional
    public void restoreMaterial(Integer billId, List<Integer> materialIdList, Integer userId, String ipAddress) {
        materialHistoryMapper
                .insertMaterialHistorys(materialIdList, billId, MaterialOperationTypeEnum.MATERIAL_REVERT.getOperationValue(), new Date());
        materialMapper.updateMaterialStatusByMaterialId(materialIdList, MaterialStatuEnum.MATERIAL_INBOUND.getStatuValue(),
                MaterialStatuEnum.MATERIAL_BORROW.getStatuValue(), new Date());
        Bill bill1 = billMapper.selectByPrimaryKey(billId);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.RESTORES_MATERIAL.getModuleName(),
                OperationLogEnum.RESTORES_MATERIAL.getContent() + bill1.getVoucherNumber(), new Date());
        operationLogMapper.insert(operationLog);
        //判断单据下的物资是否全部完成
        int total = billMapper.isRestoreMaterialAll(billId);
        if (total == 0) {
            Bill bill = Bill.builder().id(billId).billStatus(BillEnum.BILL_STATUS_HOMING.getValue()).updateTime(new Date()).build();
            billMapper.updateByPrimaryKeySelective(bill);
        }
    }
}
