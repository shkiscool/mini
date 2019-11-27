package com.dazhao.service.impl;

import com.dazhao.common.OperationLogEnum;
import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.pojo.bo.CheckBO;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.bo.MaterialProblemBO;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.dao.StocktakingRecord;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.mapper.BillMapper;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.pojo.mapper.StocktakingProblemMapper;
import com.dazhao.pojo.mapper.StocktakingRecordMapper;
import com.dazhao.pojo.mapper.MaterialMapper;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.CheckMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import com.dazhao.service.StocktakingService;
import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StocktakingServiceImpl implements StocktakingService {

    @Autowired
    private StocktakingRecordMapper stocktakingRecordMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private StocktakingProblemMapper stocktakingProblemMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private BillMapper billMapper;

    private static final String PICTURE_PATH_SEPARATOR = "|";
    private static final String REPLACE_SYMBOL = "\\|";
    private static final Integer NO_MATERIAL_LIVE_NUMBER = 0;
    private static final Integer MAX_PICTURE_NUMBER = 10;
    private static final Integer QUERY_ALL_DATA_SIGN = 1;

    @Override
    public List<StocktakingRecord> queryCheckRecords(Page page, String checkName, String beginTime, String endTime) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<StocktakingRecord> stocktakingRecordList = stocktakingRecordMapper.queryStocktakingRecord(checkName, beginTime, endTime);
        return stocktakingRecordList;
    }

    @Override
    public List<MaterialGroupVO> queryExistMaterialGroup(Page page, String materialName, Integer locationId, Integer type) {
        if (type == null || !type.equals(QUERY_ALL_DATA_SIGN)) {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
        }
        return materialMapper.queryExistMaterialGroup(materialName, locationId);
    }

    @Override
    public List<CheckMaterialVO> queryExistMaterialGroupDetail(Page page, MaterialGroupParameterBO materialGroupParameterBO, Integer type) {
        if (type == null || !type.equals(QUERY_ALL_DATA_SIGN)) {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
        }
        return materialMapper.queryExistMaterialGroupDetail(materialGroupParameterBO);
    }

    @Override
    @Transactional
    public void insertStocktakingRecordAndMaterialProblem(CheckBO checkBO, Integer userId, String ipAddress) {
        // 创建检查记录
        StocktakingRecord stocktakingRecord = StocktakingRecord.builder()
                .checkerName(checkBO.getCheckerName())
                .superintendentName(checkBO.getSuperintendentName())
                .createTime(new Date()).build();
        stocktakingRecordMapper.insertSelective(stocktakingRecord);
        List<MaterialProblemBO> materialProblemBOList = checkBO.getMaterialProblemBOList();
        if (materialProblemBOList != null && materialProblemBOList.size() > 0) {
            // 插入问题数据
            stocktakingProblemMapper.insertStocktakingMaterialProblem(stocktakingRecord.getId(), materialProblemBOList);
            // 修改物资信息
            materialMapper.updateMaterialStatusAndComment(materialProblemBOList);
            List<Integer> materialIdList = new ArrayList<>();
            materialProblemBOList.forEach(materialProblemBO -> {
                materialIdList.add(materialProblemBO.getMaterialId());
            });
            // 查询涉及单据id统计未删除的是否为0 单据id对应的未删除数量为0的话则修改单据状态deleted为1 为单据删除。
            List<BillAndMaterialVO> billAndMaterialVOS = billMapper.countLiveMaterialOfBill(materialIdList);
            for (BillAndMaterialVO billAndMaterialVO : billAndMaterialVOS) {
                if (billAndMaterialVO.getCount().equals(NO_MATERIAL_LIVE_NUMBER)) {
                    billMapper.deleteInboundBillByBillId(billAndMaterialVO.getBillId());
                }
            }
        }
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.STOCKTAKING_RECORD.getModuleName(),
                OperationLogEnum.STOCKTAKING_RECORD.getContent(), new Date());
        operationLogMapper.insert(operationLog);


    }

    @Override
    public StocktakingRecord queryCheckRecordsById(Integer id) {
        return stocktakingRecordMapper.queryStocktakingRecordById(id);
    }

    @Override
    public CommonResult updateStocktakingRecordPictureUrl(StocktakingRecord stocktakingRecord, Integer userId, String ipAddress) {
        StocktakingRecord oldchekRecords = stocktakingRecordMapper.selectByPrimaryKey(stocktakingRecord.getId());
        if (oldchekRecords == null) {
            return ResponseUtil.badArgument();
        }
        String pictureUrl = oldchekRecords.getPictureUrl();
        if (StringUtils.isEmpty(pictureUrl)) {
            stocktakingRecordMapper.updateByPrimaryKeySelective(stocktakingRecord);
        } else {
            String[] picturePath = pictureUrl.split(REPLACE_SYMBOL);
            if (picturePath.length >= MAX_PICTURE_NUMBER) {
                return ResponseUtil.fail("图片上传不能超过10张");
            }
            stocktakingRecord.setPictureUrl(pictureUrl + PICTURE_PATH_SEPARATOR + stocktakingRecord.getPictureUrl());
            stocktakingRecordMapper.updateByPrimaryKeySelective(stocktakingRecord);
        }
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.STOCKTAKING_IMAGE_UPLOAD.getModuleName(),
                OperationLogEnum.STOCKTAKING_IMAGE_UPLOAD.getContent() + oldchekRecords.getId(), new Date());
        operationLogMapper.insert(operationLog);
        return ResponseUtil.ok("添加成功");
    }

    @Override
    public boolean deleteStocktakingRecordPictureUrl(StocktakingRecord stocktakingRecord, Integer userId, String ipAddress) {
        StocktakingRecord oldcheckRecords = stocktakingRecordMapper.selectByPrimaryKey(stocktakingRecord.getId());
        if (oldcheckRecords == null) {
            return false;
        }
        String pictureUrl = oldcheckRecords.getPictureUrl();
        if (StringUtils.isEmpty(pictureUrl)) {
            return false;
        }
        String[] pathArr = pictureUrl.split(REPLACE_SYMBOL);
        List<String> pathList = new ArrayList<>(pathArr.length);
        Collections.addAll(pathList, pathArr);
        boolean isSuccess = pathList.removeIf(e -> e.equals(stocktakingRecord.getPictureUrl()));
        if (!isSuccess) {
            return false;
        } else {
            String newPictureUrl = String.join(PICTURE_PATH_SEPARATOR, pathList);
            stocktakingRecord.setPictureUrl(newPictureUrl);
            stocktakingRecordMapper.updateByPrimaryKeySelective(stocktakingRecord);
            OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.STOCKTAKING_IMAGE_DELETE.getModuleName(),
                    OperationLogEnum.STOCKTAKING_IMAGE_DELETE.getContent() + oldcheckRecords.getId(), new Date());
            operationLogMapper.insert(operationLog);
            return true;
        }
    }

    @Override
    public void uploadStocktakingSignature(String signatureHash, String urlPath, Integer billId) {
        stocktakingRecordMapper.uploadStocktakingSignature(signatureHash, urlPath, billId);
    }
}
