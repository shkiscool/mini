package com.dazhao.controller;

import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.pojo.dao.MaterialSort;
import com.dazhao.service.MaterialService;
import com.dazhao.service.MaterialSortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "物资分类相关")
@RequestMapping(value = "/sorts")
public class MaterialSortController {

    @Autowired
    private MaterialSortService materialSortService;


    @GetMapping("/")
    @ApiOperation(value = "获取物资分类信息")
    public CommonResult queryMaterialSortList() {
        List<MaterialSort> materialSorts = materialSortService.queryMaterialSortList();
        return ResponseUtil.okList(materialSorts);
    }

    @PostMapping("/")
    @ApiOperation(value = "添加物资类别信息")
    public CommonResult addMaterialSort(@RequestBody MaterialSort materialSort, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        boolean isExist = materialSortService.isExistMaterialSortByName(materialSort.getSortName());
        if (isExist) {
            return ResponseUtil.fail("已经存在分类名称");
        }
        materialSortService.addMaterialSort(materialSort, userId, ipAddress);
        return ResponseUtil.ok("创建成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除类别信息")
    public CommonResult deleteMaterialSortById(@PathVariable(value = "id") Integer materialSortId, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        //判断是否该物资分类是否存在使用
        boolean isUse = materialSortService.isUseMaterialSort(materialSortId);
        if (isUse) {
            materialSortService.deleteMaterialSortById(materialSortId, userId, ipAddress);
            return ResponseUtil.ok("删除成功");
        }
        return ResponseUtil.fail("删除失败-该物资分类存在使用");
    }
}
