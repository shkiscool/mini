package com.dazhao.service.impl;

import com.dazhao.common.OperationLogEnum;
import com.dazhao.common.Page;
import com.dazhao.pojo.bo.ChoiceMaterialParameterBO;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.bo.MaterialGroupQueryConditionBO;
import com.dazhao.pojo.bo.UpdateLocationBO;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.mapper.BillMapper;
import com.dazhao.pojo.mapper.LocationMapper;
import com.dazhao.pojo.mapper.MaterialHistoryMapper;
import com.dazhao.pojo.mapper.MaterialMapper;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.pojo.vo.BillAndMaterialDetailsVO;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.ChoiceBillVO;
import com.dazhao.pojo.vo.DestoryMaterialGroupAndMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import com.dazhao.service.MaterialService;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private LocationMapper locationMapper;

    /**
     * 根据物资编码获取单个物资信息
     *
     * @param materialNumber 物资编码
     * @return 物资信息
     */
    @Override
    public Material getMaterialByMaterialNumber(String materialNumber) {
        return materialMapper.getMaterialByMaterialNumber(materialNumber);
    }



    @Override
    public List<Material> queryInboundMaterialGroupDetailByConditon(Page page, MaterialGroupParameterBO materialGroupParameterBO) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return materialMapper.queryInboundMaterialGroupDetailByConditon(materialGroupParameterBO);
    }

    @Override
    public List<ChoiceBillVO> queryCanDestoryBill(Page page, ChoiceMaterialParameterBO choiceMaterialParameterBO) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return materialMapper.queryCanDestoryBill(choiceMaterialParameterBO);
    }

    @Override
    public DestoryMaterialGroupAndMaterialVO queryDestoryMaterialByBillId(Integer[] billIds) {
        List<MaterialGroupVO> materialGroupVOList = materialMapper.queryDestoryMaterialGroupByBillId(billIds);
        List<Material> materialList = materialMapper.queryDestoryMaterial(billIds);
        return new DestoryMaterialGroupAndMaterialVO(materialGroupVOList, materialList);
    }


    @Override
    public int checkMatrialStatusIfSatisfies(Integer materialStatus, List<Integer> materialIdList) {
        return materialMapper.checkMatrialStatusIfSatisfies(materialStatus, materialIdList);
    }

    @Override
    public List<MaterialGroupVO> queryMaterialGroupByCondition(Page page, MaterialGroupQueryConditionBO materialGroupQueryConditionBO) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return materialMapper.queryMaterialGroupByCondition(materialGroupQueryConditionBO);
    }

    @Override
    public List<BillAndMaterialVO> queryMaterialGroupDetailsByCondition(Page page, MaterialGroupParameterBO materialGroupParameterBO) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return materialMapper.queryMaterialGroupDetailsByCondition(materialGroupParameterBO);
    }

    @Override
    public BillAndMaterialDetailsVO queryMaterialAndBillDetialsByMaterialId(Integer materialId) {
        return materialMapper.queryMaterialAndBillDetialsByMaterialId(materialId);
    }

    @Override
    @Transactional
    public void updateMaterialLocation(UpdateLocationBO updateLocationBO, Integer operationNameId, String ipAddress) {
        // 批量修改物资仓位id
        materialMapper.updateMaterialLocation(updateLocationBO.getLocationId(), updateLocationBO.getMaterailList());
        Location location = locationMapper.selectByPrimaryKey(updateLocationBO.getLocationId());
        // 添加物资仓位修改的物资追溯
        materialHistoryMapper
                .insertUploadLocationMaterialHistorys(location.getLocationNumber(), updateLocationBO.getMaterailList(), operationNameId, new Date());
        OperationLog operationLog = new OperationLog(operationNameId, ipAddress, OperationLogEnum.UPDATE_LOCATION.getModuleName(),
                OperationLogEnum.UPDATE_LOCATION.getContent(), new Date());
        operationLogMapper.insert(operationLog);
    }

    @Override
    public List<MaterialGroupVO> queryMaterialGroupByConditionExcel(MaterialGroupQueryConditionBO materialGroupQueryConditionBO) {
        return materialMapper.queryMaterialGroupByCondition(materialGroupQueryConditionBO);
    }

    @Override
    public boolean updateMaterialPrintStatus(String materialNumber) {
        return materialMapper.updateMaterialPrintStatus(materialNumber) > 0;
    }


}
