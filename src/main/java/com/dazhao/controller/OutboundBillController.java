package com.dazhao.controller;


import com.dazhao.common.MaterialStatuEnum;
import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.CheckParaUtils;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.external.archive.WitnessCloudService;
import com.dazhao.pojo.bo.OutBillBO;
import com.dazhao.service.BillService;
import com.dazhao.service.MaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 出库单
 */
@Api(description = "出库单")
@RestController
@RequestMapping(value = "/outbound_bill")
public class OutboundBillController {

    @Autowired
    private BillService billService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private WitnessCloudService witnessCloudService;

    @GetMapping("/")
    @ApiOperation(value = "查询出库（借阅，移交，销毁）列表", httpMethod = "GET", produces = "application/json", response = CommonResult.class)
    public CommonResult listBills(@ApiParam(name = "voucherNumber", value = "凭证单号")
            @RequestParam(name = "voucherNumber", required = false) String voucherNumber,
            @ApiParam(name = "handOverName", value = "移交人") @RequestParam(name = "handOverName", required = false) String handOverName,
            @ApiParam(name = "receivedDepartment", value = "接收单位")
            @RequestParam(name = "receivedDepartment", required = false) String receivedDepartment,
            @ApiParam(name = "beginTime", value = "开始时间") @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "结束时间") @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "billStatus", value = "单据状态(1：借阅中、2：已归还、3：待出库、4：已完成)")
            @RequestParam(name = "billStatus", required = false) Integer billStatus,
            @ApiParam(name = "billType", value = "单据类型：2：借阅、3：移交、4：销毁", required = true)
            @RequestParam(name = "billType") Integer billType, Page page) {

        if (!CheckParaUtils.isNullAndNumAndEnglishJ(voucherNumber)) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        if (billStatus != null && !CheckParaUtils.isNumberJ(String.valueOf(billStatus))) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }

        return ResponseUtil.okList(billService.listBills(page,
                voucherNumber != null ? voucherNumber.trim() : null,
                handOverName != null ? handOverName.trim() : null,
                receivedDepartment != null ? receivedDepartment.trim() : null,
                beginTime, endTime, billStatus, billType));
    }

    @PostMapping(value = "/")
    @ApiOperation(value = "（借阅、移交、销毁 统一的提交接口）")
    public CommonResult insertBillAndMaterialHistory(@Valid @RequestBody OutBillBO outBillBO, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        // 去重
        List<Integer> materialIdList = outBillBO.getMaterialIdList().stream().distinct().collect(Collectors.toList());
        outBillBO.setMaterialIdList(materialIdList);
        // 判断物资当前是否在库 不是在库不满足其他操作。
        int total = materialService.checkMatrialStatusIfSatisfies(MaterialStatuEnum.MATERIAL_INBOUND.getStatuValue(), materialIdList);
        if (outBillBO.getMaterialIdList().size() != total) {
            return ResponseUtil.fail("-1", "物资中存在不满足出库的物资");
        }
        Boolean isSuccess = billService.insertBillAndMaterialHistory(outBillBO, userId, ipAddress);
        if (isSuccess) {
            witnessCloudService.uploadFileExcel();
            return ResponseUtil.ok("提交成功");
        }
        return ResponseUtil.fail("提交失败");
    }

}
