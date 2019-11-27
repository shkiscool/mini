package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.CheckParaUtils;
import com.dazhao.pojo.dao.MaterialHistory;
import com.dazhao.pojo.vo.MaterialHistoryInformationVO;
import com.dazhao.service.BillService;
import com.dazhao.service.MaterialHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物资追溯
 */
@RestController
@Api(description = "物资追溯")
@RequestMapping(value = "/material_history")
public class MaterialHistoryController {

    @Autowired
    private MaterialHistoryService materialHistoryService;

    @Autowired
    private BillService billService;

    @GetMapping("/{voucher_number}/{operation_type}")
    @ApiOperation(value = "查询物资分组详情（借阅，移交，销毁）", httpMethod = "GET", produces = "application/json", response = CommonResult.class)
    public CommonResult listMaterialGroupByMaterialNumber(Page page, @ApiParam(required = true, name = "voucher_number",
            value = "凭证单号（借阅，移交，销毁）") @PathVariable("voucher_number") String voucherNumber,
            @ApiParam(required = true, name = "operation_type", value = "操作类型(2：借阅、3：移交、4：销毁)")
            @PathVariable("operation_type") Integer operationType) {
        if (!CheckParaUtils.isNotNull(voucherNumber) || !CheckParaUtils.isNumAndEnglishJ(voucherNumber.trim())) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        return ResponseUtil.okList(materialHistoryService.listMaterialGroup(page, voucherNumber.trim(), operationType), billService
                .queryBillByVoucherNumber(voucherNumber.trim()));
    }


    @GetMapping("/material_details")
    @ApiOperation(value = "查询物资明细列表（借阅，移交，销毁）", httpMethod = "GET", produces = "application/json", response = CommonResult.class)
    public CommonResult listMaterialDetails(Page page, @ApiParam(name = "voucherNumber", value = "凭证单号", required = true)
            @RequestParam("voucherNumber") String voucherNumber,
            @ApiParam(name = "materialName", value = "没收实物名称", required = true) @RequestParam("materialName") String materialName,
            @ApiParam(name = "materialUnit", value = "计量单位", required = true) @RequestParam("materialUnit") String materialUnit,
            @ApiParam(name = "materialType", value = "型号") @RequestParam(name = "materialType", required = false) String materialType,
            @ApiParam(name = "locationNumber", value = "仓位编号", required = true) @RequestParam("locationNumber") String locationNumber,
            @ApiParam(name = "materialComment", value = "物资备注") @RequestParam(name = "materialComment", required = false) String materialComment,
            @ApiParam(name = "sortName", value = "分类名称", required = true) @RequestParam("sortName") String sortName,
            @ApiParam(name = "operationType", value = "操作类型（2：借阅，3：移交，4：销毁）", required = true)
            @RequestParam("operationType") Integer operationType) {
        if (!CheckParaUtils.isNotNull(voucherNumber) || !CheckParaUtils.isNumAndEnglishJ(voucherNumber.trim())) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        if (!CheckParaUtils.isNotNull(locationNumber)) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        if (!CheckParaUtils.isNotNull(materialUnit)) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        if (!CheckParaUtils.isNotNull(materialName)) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        if (!CheckParaUtils.isNotNull(sortName)) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        if (materialType == null || "".equals(materialType.trim())) {
            materialType = "-";
        }
        if (materialComment == null || "".equals(materialComment.trim())) {
            materialComment = "-";
        }
        return ResponseUtil.okList(materialHistoryService
                .listMaterialDetails(page, voucherNumber.trim(), materialName.trim(), materialUnit.trim(), materialType.trim(), locationNumber.trim(),
                        materialComment.trim(), operationType, sortName.trim()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据物资ID查询单个物资追溯信息", notes = "根据操作类型进行遍历：操作类型(1：入库、2：借阅、3：移交、4：销毁、5：归还、6：仓位变更)")
    public CommonResult queryMaterialHistoryById(@ApiParam(name = "id", value = "物资id") @PathVariable("id") Integer materialId) {
        List<MaterialHistoryInformationVO> materialHistoryList = materialHistoryService.queryMaterialHistoryById(materialId);
        return ResponseUtil.okList(materialHistoryList);

    }
}
