package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.CheckParaUtils;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.external.archive.WitnessCloudService;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.vo.MaterialGroupAndMateialsVO;
import com.dazhao.service.RestoresService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "借阅归还相关接口")
@RequestMapping("/restores")
public class RestoresController {

    @Autowired
    private RestoresService restoresService;

    @Autowired
    private WitnessCloudService witnessCloudService;

    @ApiOperation(value = "查询出所有借阅单据单据的状态为借阅中")
    @GetMapping("/")
    public CommonResult queryBillByBorrowStatus(Page page) {
        List<Bill> borrowBill = restoresService.queryBillByBorrowStatus(page);
        return ResponseUtil.okList(borrowBill);
    }

    @ApiOperation(value = "根据借阅单id查询该id下的物资分组信息及分组物资信息")
    @GetMapping("/{bill_id}")
    @ApiImplicitParam(paramType = "path", name = "bill_id", value = "借阅单据id", dataType = "Integer")
    public CommonResult queryBillByBorrowStatus(@PathVariable(value = "bill_id") Integer billId) {
        if (billId == null || !CheckParaUtils.isNumberJ(String.valueOf(billId))) {
            return ResponseUtil.badArgument();
        }
        List<MaterialGroupAndMateialsVO> materialGroupAndMateialsVOS = restoresService.queryBorrowBillMaterialGroupInformation(billId);
        return ResponseUtil.okList(materialGroupAndMateialsVOS);
    }


    @PostMapping("/{bill_id}")
    @ApiOperation(value = "物资归还")
    public CommonResult restoreMaterials(
            @ApiParam(required = true, name = "bill_id", value = "借阅单据id")
            @PathVariable(value = "bill_id") Integer billId,
            @ApiParam(required = true, name = "materialIdList", value = "物资id集合")
            @RequestBody List<Integer> materialIdList, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        if (billId == null || materialIdList == null || materialIdList.size() == 0) {
            return ResponseUtil.badArgument();
        }
        //判断 物资id是否属于该借阅单物资并且物资状态是否为借阅
        List<Integer> newMaterialIdList = materialIdList.stream().distinct().collect(Collectors.toList());
        int total = restoresService.borrowMaterialExists(billId, newMaterialIdList);
        if (total != materialIdList.size()) {
            return ResponseUtil.fail("-1", "提交的物资存在不属于该单据下的物资或已归还的物资");
        }
        // 添加物资历史
        restoresService.restoreMaterial(billId, newMaterialIdList, userId, ipAddress);
        witnessCloudService.uploadFileExcel();
        return ResponseUtil.ok();
    }

}
