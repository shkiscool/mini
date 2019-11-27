package com.dazhao.controller;

import com.dazhao.common.DefaultEnum;
import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.external.archive.WitnessCloudService;
import com.dazhao.external.law.PostMaterialInfoRequestService;
import com.dazhao.pojo.bo.InboundBillAndMaterialGroupBO;
import com.dazhao.pojo.bo.InboundBillQueryParameterBO;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.MaterialGroupVO;
import com.dazhao.service.BillService;
import com.dazhao.service.LocationService;
import com.dazhao.service.MaterialService;
import com.dazhao.service.MaterialSortService;
import com.dazhao.service.MaterialSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(description = "入库相关接口")
@RequestMapping(value = "/inbound_bill")
public class InboundBillController {

    @Autowired
    private BillService billService;
    @Autowired
    private PostMaterialInfoRequestService postMaterialInfoRequestService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialSortService materialSortService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MaterialSourceService materialSourceService;
    @Autowired
    private WitnessCloudService witnessCloudService;

    @PostMapping(value = "/")
    @ApiOperation(value = "创建入库单及入库单下的物资信息", notes = "型号、备注不是必填。参数错误返回相关错误信息")
    @ApiImplicitParam(paramType = "body", name = "inboundBillAndMaterialGroupBo", value = "入库单、物资信息", required = true, dataType = "InboundBillAndMaterialGroupBO")
    public CommonResult createInboundBillAndMaterials(@Validated @RequestBody InboundBillAndMaterialGroupBO inboundBillAndMaterialGroupBo,
            HttpServletRequest request) {
        if (!(inboundBillAndMaterialGroupBo != null && inboundBillAndMaterialGroupBo.getMaterialGroup() != null)) {
            return ResponseUtil.badArgument();
        }
        for (Material material : inboundBillAndMaterialGroupBo.getMaterialGroup()) {
            if (material.getSortId() == null || material.getMaterialName() == null || material.getTotal() == null
                    || material.getLocationId() == null || !(0 < material.getTotal() && material.getTotal() <= DefaultEnum.DEFAULT_MATERIAL_MAX
                    .getDefaultValue()) || !(materialSortService.isExistMaterialSortById(material.getSortId())) || !(locationService
                    .isExistLocationByid(material.getLocationId()))) {
                return ResponseUtil.badArgument();
            }
        }
        if (!materialSourceService.isExistMaterialSourceById(inboundBillAndMaterialGroupBo.getMaterialSourceId())) {
            return ResponseUtil.badArgument();
        }
        Integer userId = JWTUtil.getUserId(request);
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        Bill bill = billService.createInboundBillAndMaterials(inboundBillAndMaterialGroupBo, userId, ipAdrress);
        witnessCloudService.uploadFileExcel();
        try {
            postMaterialInfoRequestService.post(inboundBillAndMaterialGroupBo, bill);
        } catch (IOException e) {
            log.error("推送罚没物资到法眼异常：", e);
        }
        return ResponseUtil.ok(bill);
    }

    @GetMapping(value = "/materials/{id}")
    @ApiOperation(value = "根据入库单id查询该单据下的所有物资")
    public CommonResult queryMaterialListByInboundBillId(@PathVariable(value = "id") Integer id) {
        List<Material> materialList = billService.queryMaterialListByInboundBillId(id);
        return ResponseUtil.okList(materialList);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "入库单条件查询", notes = "查询的条件有值才拼接到路径后面")
    public CommonResult queryInboundBillList(Page page, InboundBillQueryParameterBO inboundBillQueryParameterBo) {
        List<Bill> bills = billService.queryInboundBillList(page, inboundBillQueryParameterBo);
        return ResponseUtil.okList(bills);
    }

    @GetMapping(value = "/detail/{bill_id}")
    @ApiOperation(value = "入库单据详情查询")
    public CommonResult queryInboundBillDetailOne(@PathVariable(value = "bill_id") Integer id) {
        Bill bill = billService.queryInboundBillDetail(id);
        return ResponseUtil.ok(bill);
    }

    @GetMapping(value = "/material_group/{bill_id}")
    @ApiOperation(value = "入库单据下的物资分组")
    public CommonResult queryInboundBillMaterialGroup(@PathVariable(value = "bill_id") Integer id, Page page) {
        List<MaterialGroupVO> materialGroup = billService.queryInboundBillMaterialGroup(id, page);
        return ResponseUtil.okList(materialGroup);
    }

    @GetMapping("/material_group_detail")
    @ApiOperation(value = "根据条件查询物资分组下的物资详情", notes = "该接口需要传:单据id、实物名称、备注、型号、计量单位、仓位id、分类id这几个参数")
    public CommonResult queryMaterialGroupDetailByConditon(Page page, MaterialGroupParameterBO materialGroupParameterBO) {
        if (materialGroupParameterBO.getBillId() == null
                || StringUtils.isEmpty(materialGroupParameterBO.getMaterialName())
                || StringUtils.isEmpty(materialGroupParameterBO.getMaterialComment())
                || StringUtils.isEmpty(materialGroupParameterBO.getMaterialType())
                || StringUtils.isEmpty(materialGroupParameterBO.getMaterialUnit())
                || materialGroupParameterBO.getLocationId() == null
                || materialGroupParameterBO.getSortId() == null) {
            return ResponseUtil.badArgument();
        }
        List<Material> materialList = materialService.queryInboundMaterialGroupDetailByConditon(page, materialGroupParameterBO);
        return ResponseUtil.okList(materialList);
    }

    @PatchMapping("/consummation/{bill_id}")
    @ApiOperation(value = "打印完成,点击完成修改入库单据信息")
    public CommonResult updateConsummationBillStatus(
            @NotNull(message = "入库单据id不能为空") @ApiParam(name = "bill_id", value = "入库单据id") @PathVariable(value = "bill_id") Integer billId,
            HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        Boolean isSuccess = billService.updateConsummationBillStatus(billId, userId, ipAdrress);
        if (isSuccess) {
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail("单据存在物资未打印RFID");
    }

}


