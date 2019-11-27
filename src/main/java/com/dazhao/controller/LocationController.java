package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.dao.Material;
import com.dazhao.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "仓位相关接口")
@RequestMapping(value = "/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping(value = "/")
    @ApiOperation(value = "查询所有的仓位信息")
    public CommonResult queryLocationList(Page page) {
        List<Location> locationList = locationService.queryLocationList(page);
        return ResponseUtil.okList(locationList);
    }

    @GetMapping(value = "/default")
    @ApiOperation(value = "查询默认推荐仓位")
    public CommonResult queryDefaultLocation() {
        Location location = locationService.queryDefaultLocation();
        return ResponseUtil.ok(location);
    }

    @PatchMapping(value = "/{id}/{flag}")
    @ApiOperation(value = "更新仓位是否可存放状态", notes = "0表示该仓位可存放，1表示该仓位已满")
    public CommonResult updateLocationFillFlag(@PathVariable(value = "id") Integer locationId, @PathVariable(value = "flag") Integer fillFlag) {
        if (fillFlag.equals(0) || fillFlag.equals(1)) {
            locationService.updateLocationFillFlag(locationId, fillFlag);
            return ResponseUtil.ok();
        }
        return ResponseUtil.badArgument();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据仓位id查询该仓位下统计相同物资分组信息")
    public CommonResult queryLocationMaterialGroupByid(
            @ApiParam(required = true, name = "id", value = "仓位id")
            @PathVariable(value = "id") Integer locationId) {
        List<String> materialList = locationService.queryLocationMaterialGroupByid(locationId);
        return ResponseUtil.okList(materialList);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "根据仓位id逻辑删除仓位")
    public CommonResult deleteLocationByid(@PathVariable("id") Integer locationId, HttpServletRequest request) {
        Boolean isExist = locationService.isExistUserLocationByid(locationId);
        if (isExist) {
            return ResponseUtil.fail("该仓位下存在物资，无法删除");
        }
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        locationService.deleteLocationByid(locationId, userId, ipAddress);
        return ResponseUtil.ok();
    }

    @PostMapping(value = "/")
    @ApiOperation(value = "添加仓位")
    public CommonResult insertLocation(@Validated @RequestBody Location location, HttpServletRequest request) {
        Boolean isExist = locationService.isExistLocationByLocationNumber(location.getLocationNumber());
        if (isExist) {
            return ResponseUtil.fail("仓位编号已存在");
        }
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        locationService.insertLocation(location, userId, ipAddress);
        return ResponseUtil.ok();
    }
}
