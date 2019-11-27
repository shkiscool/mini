package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.CheckParaUtils;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.bo.MaterialGroupQueryConditionBO;
import com.dazhao.pojo.bo.UpdateLocationBO;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.BillAndMaterialDetailsVO;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import com.dazhao.service.LocationService;
import com.dazhao.service.MaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物资详情
 */
@RestController
@RequestMapping(value = "/material")
@Api(description = "物资详情")
@Slf4j
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private LocationService locationService;

    @GetMapping("/{material_number}")
    @ApiOperation(value = "获取单条物资信息", httpMethod = "GET", produces = "application/json", response = CommonResult.class)
    public CommonResult getMaterialByMaterialNumber(@ApiParam(required = true, name = "material_number",
            value = "物资编号") @PathVariable("material_number") String materialNumber) {
        if (!CheckParaUtils.isNotNull(materialNumber) || !CheckParaUtils.isNumberJ(materialNumber.trim())) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        return ResponseUtil.ok(materialService.getMaterialByMaterialNumber(materialNumber.trim()));
    }


    @GetMapping("/group")
    @ApiOperation(value = "物资分组查询")
    public CommonResult queryMaterialGroupByCondition(Page page, MaterialGroupQueryConditionBO materialGroupQueryConditionBO) {
        List<MaterialGroupVO> materialGroupVOS = materialService.queryMaterialGroupByCondition(page, materialGroupQueryConditionBO);
        return ResponseUtil.okList(materialGroupVOS);
    }

    @GetMapping(value = "/group/export")
    @ApiOperation(value = "物资分组查询Excel导出数据")
    public CommonResult queryMaterialGroupByConditionExcel(MaterialGroupQueryConditionBO materialGroupQueryConditionBO,
            HttpServletResponse response) {
        List<MaterialGroupVO> materialGroupVOS = materialService.queryMaterialGroupByConditionExcel(materialGroupQueryConditionBO);
        return ResponseUtil.ok(materialGroupVOS);
    }

    @GetMapping("/group/details")
    @ApiOperation(value = "物资分组下的详情", notes = "分类id、没收实物名称、计量单位、型号、仓位id、入库时间、物资状态、物资入库源id 必须要传")
    public CommonResult queryMaterialGroupDetailsByCondition(Page page, MaterialGroupParameterBO materialGroupParameterBO) {
        List<BillAndMaterialVO> billAndMaterialVO = materialService.queryMaterialGroupDetailsByCondition(page, materialGroupParameterBO);
        return ResponseUtil.okList(billAndMaterialVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation(value = "查询物资详情")
    public CommonResult queryMaterialAndBillDetialsByMaterialId(
            @ApiParam(name = "id", value = "物资id") @PathVariable(value = "id") Integer materialId) {
        BillAndMaterialDetailsVO billAndMaterialDetailsVO = materialService.queryMaterialAndBillDetialsByMaterialId(materialId);
        return ResponseUtil.ok(billAndMaterialDetailsVO);
    }

    @PutMapping("/location")
    @ApiOperation(value = "批量修改物资仓位", notes = "需要传要修改为的仓位id，物资集合里面需要传物资id，物资仓位id,和物资仓位名称")
    public CommonResult updateMaterialLocation(@Validated @RequestBody UpdateLocationBO updateLocationBO, HttpServletRequest request) {
        Boolean isExistLocation = locationService.isExistLocationByid(updateLocationBO.getLocationId());
        if (!isExistLocation) {
            return ResponseUtil.badArgument();
        }
        for (Material material : updateLocationBO.getMaterailList()) {
            if (material.getId() == null || material.getLocationId() == null || material.getLocationNumber() == null) {
                return ResponseUtil.badArgument();
            }
        }
        Integer opreationNameId = JWTUtil.getUserId(request);
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        materialService.updateMaterialLocation(updateLocationBO, opreationNameId, ipAdrress);
        return ResponseUtil.ok();
    }

    @PatchMapping("/print/{material_number}")
    @ApiOperation(value = "根据物资编号更新物资打印状态为打印")
    public CommonResult updateMaterialPrintStatus(
            @NotBlank(message = "物资编号不能为空") @PathVariable(value = "material_number") @ApiParam(name = "material_number", value = "物资编号") String materialNumber) {
        boolean isSuccess = materialService.updateMaterialPrintStatus(materialNumber);
        if (isSuccess) {
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }


}