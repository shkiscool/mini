package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.pojo.bo.ChoiceMaterialParameterBO;
import com.dazhao.pojo.vo.ChoiceBillVO;
import com.dazhao.pojo.vo.DestoryMaterialGroupAndMaterialVO;
import com.dazhao.service.MaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "物资销毁相关接口")
@RequestMapping(value = "/destory")
public class DestoryBillController {

    @Autowired
    private MaterialService materialService;


    @GetMapping(value = "/")
    @ApiOperation(value = "销毁查询符合销毁的入库单据接口")
    public CommonResult queryDestoryMaterial(Page page, ChoiceMaterialParameterBO choiceMaterialParameterBO) {
        List<ChoiceBillVO> choiceBillVOList = materialService.queryCanDestoryBill(page, choiceMaterialParameterBO);
        return ResponseUtil.okList(choiceBillVOList);
    }

    @GetMapping(value = "/material")
    @ApiOperation(value = "查询选择的单据物资信息分组和所有物资", notes = "xxxx?id=38&id=39,物资状态需要根据物资状态id前端自己判断状态（ 1：在库、2：借阅、3：移交、4：销毁、5：冻结、6：异常） ")
    public CommonResult queryDestoryMaterialByBillId(
            @NotNull(message = "单据数组不能为空") @ApiParam(name = "id", value = "单据Id数组") @RequestParam(value = "id") Integer[] billIds) {
        DestoryMaterialGroupAndMaterialVO destoryMaterialGroupAndMaterialVO = materialService.queryDestoryMaterialByBillId(billIds);
        return ResponseUtil.ok(destoryMaterialGroupAndMaterialVO);
    }

}
