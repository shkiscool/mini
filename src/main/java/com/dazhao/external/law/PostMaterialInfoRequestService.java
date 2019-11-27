package com.dazhao.external.law;

import com.alibaba.fastjson.JSONObject;
import com.dazhao.pojo.bo.InboundBillAndMaterialGroupBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.dao.MaterialSource;
import com.dazhao.pojo.mapper.BillMapper;
import com.dazhao.pojo.mapper.LocationMapper;
import com.dazhao.pojo.mapper.MaterialSourceMapper;
import com.dazhao.pojo.vo.MaterialGroupVO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PostMaterialInfoRequestService {

    @Value(value = "${law.post-material}")
    private String postMaterialUrl;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 法眼请求成功的状态值
     */
    private static final String SUCCESSS_TATUS = "1";

    @Autowired
    private MaterialSourceMapper materialSourceMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private BillMapper billMapper;

    /**
     * 罚没物资入库物资发送到法度平台
     */
    @Async
    public void post(InboundBillAndMaterialGroupBO inboundBillAndMaterialGroupBo, Bill bill) throws IOException {
        List<ForfeitureGoodsAddModel> forfeitureGoodsAddModels = getForfeitureGoodsAddModelList(inboundBillAndMaterialGroupBo);
        billMapper.updateWetherSyncStatus(bill.getId());
        boolean isSuccesss = post(forfeitureGoodsAddModels);
        if (isSuccesss) {
            billMapper.updateWetherSyncStatus(bill.getId());
        }
    }

    /**
     * 同步罚没物资入库物资发送到法度平台
     *
     * @param billId 单据id
     */
    public void post(Integer billId) throws IOException {
        Bill bill = billMapper.queryInboundBillById(billId);
        List<MaterialGroupVO> materialGroupVOList = billMapper.queryInboundMaterialGroup(billId);
        List<ForfeitureGoodsAddModel> forfeitureGoodsAddModels = getForfeitureGoodsAddModelListByBillId(bill, materialGroupVOList);
        boolean isSuccesss = post(forfeitureGoodsAddModels);
        if (isSuccesss) {
            billMapper.updateWetherSyncStatus(bill.getId());
        }
    }

    /**
     * 发送入库物资请求
     *
     * @param forfeitureGoodsAddModels 入库物资集合
     */
    private boolean post(List<ForfeitureGoodsAddModel> forfeitureGoodsAddModels) throws IOException {
        String bodyString = JSONObject.toJSONString(forfeitureGoodsAddModels);
        Content content = Request
                .Post(postMaterialUrl)
                .bodyString(bodyString, ContentType.APPLICATION_JSON)
                .execute()
                .returnContent();
        JSONObject resultObject = JSONObject.parseObject(content.asString());
        System.out.println(resultObject);
        String status = String.valueOf(resultObject.get("status"));
        if (status.equals(SUCCESSS_TATUS)) {
            return true;
        }
        return false;
    }

    private List<ForfeitureGoodsAddModel> getForfeitureGoodsAddModelList(InboundBillAndMaterialGroupBO inboundBillAndMaterialGroupBo) {
        List<ForfeitureGoodsAddModel> forfeitureGoodsAddModels = new ArrayList<>();
        MaterialSource materialSource = materialSourceMapper.selectByPrimaryKey(inboundBillAndMaterialGroupBo.getMaterialSourceId());
        List<Material> materialGroup = inboundBillAndMaterialGroupBo.getMaterialGroup();
        materialGroup.forEach(material -> {
            Location location = locationMapper.selectByPrimaryKey(material.getLocationId());
            ForfeitureGoodsAddModel forfeitureGoodsAddModel = new ForfeitureGoodsAddModel();
            forfeitureGoodsAddModel.setActionCause(inboundBillAndMaterialGroupBo.getReason());
            forfeitureGoodsAddModel.setCaseHandlerName(inboundBillAndMaterialGroupBo.getConsignerName());
            forfeitureGoodsAddModel.setCaseNumber(inboundBillAndMaterialGroupBo.getCaseNumber());
            forfeitureGoodsAddModel.setConfiscationName(material.getMaterialName());
            forfeitureGoodsAddModel.setForfeitureDate(sdf.format(inboundBillAndMaterialGroupBo.getConfiscateDate()));
            forfeitureGoodsAddModel.setForfeitureObjName(inboundBillAndMaterialGroupBo.getConfiscateName());
            forfeitureGoodsAddModel.setInWarehouseSource(materialSource.getSourceName());
            forfeitureGoodsAddModel.setMeasurementUnit(StringUtils.isEmpty(material.getMaterialUnit()) ? "-" : material.getMaterialUnit());
            forfeitureGoodsAddModel.setModel(StringUtils.isEmpty(material.getMaterialType()) ? "-" : material.getMaterialType());
            forfeitureGoodsAddModel.setQuantity(String.valueOf(material.getTotal()));
            forfeitureGoodsAddModel.setReceiverName(inboundBillAndMaterialGroupBo.getRecipientName());
            forfeitureGoodsAddModel.setRemark(material.getMaterialComment());
            forfeitureGoodsAddModel.setWarehousePositionId(location.getLocationNumber());
            forfeitureGoodsAddModels.add(forfeitureGoodsAddModel);
        });
        return forfeitureGoodsAddModels;
    }

    private List<ForfeitureGoodsAddModel> getForfeitureGoodsAddModelListByBillId(Bill bill, List<MaterialGroupVO> materialGroupVOList) {
        List<ForfeitureGoodsAddModel> forfeitureGoodsAddModels = new ArrayList<>();
        materialGroupVOList.forEach(material -> {
            ForfeitureGoodsAddModel forfeitureGoodsAddModel = new ForfeitureGoodsAddModel();
            forfeitureGoodsAddModel.setActionCause(bill.getReason());
            forfeitureGoodsAddModel.setCaseHandlerName(bill.getConsignerName());
            forfeitureGoodsAddModel.setCaseNumber(bill.getCaseNumber());
            forfeitureGoodsAddModel.setConfiscationName(material.getMaterialName());
            forfeitureGoodsAddModel.setForfeitureDate(sdf.format(bill.getConfiscateDate()));
            forfeitureGoodsAddModel.setForfeitureObjName(bill.getConfiscateName());
            forfeitureGoodsAddModel.setInWarehouseSource(bill.getSourceName());
            forfeitureGoodsAddModel.setMeasurementUnit(material.getMaterialUnit());
            forfeitureGoodsAddModel.setModel(material.getMaterialType());
            forfeitureGoodsAddModel.setQuantity(String.valueOf(material.getTotal()));
            forfeitureGoodsAddModel.setReceiverName(bill.getRecipientName());
            forfeitureGoodsAddModel.setRemark(material.getMaterialComment());
            forfeitureGoodsAddModel.setWarehousePositionId(material.getLocationNumber());
            forfeitureGoodsAddModels.add(forfeitureGoodsAddModel);
        });
        return forfeitureGoodsAddModels;
    }
}
