package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.CheckParaUtils;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.pojo.dao.MaterialSource;
import com.dazhao.service.MaterialSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "入库源")
@RequestMapping(value = "/sources")
public class MaterialSourceController {

    @Autowired
    private MaterialSourceService materialSourceService;

    @PostMapping("/{sourceName}/{keepDays}")
    @ApiOperation(value = "新增入库源", httpMethod = "POST", produces = "application/json", response = CommonResult.class)
    public CommonResult insertSource(@ApiParam(required = true, name = "sourceName", value = "入库源名称") @PathVariable("sourceName") String sourceName,
            @ApiParam(required = true, name = "keepDays", value = "提醒时间 (0代表无期限,单位天)", defaultValue = "0")
            @PathVariable("keepDays") Integer keepDays, HttpServletRequest request) {
        if (!CheckParaUtils.isNotNull(sourceName)) {
            return ResponseUtil.fail(ErrorCodeEnum.PARAM_ERROR.getErrorCode(), ErrorCodeEnum.PARAM_ERROR.getErrorMsg());
        }
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        materialSourceService.insertSource(MaterialSource.builder()
                .sourceName(sourceName.trim())
                .keepDays(keepDays)
                .createTime(new Date())
                .deleted(0)
                .build(), userId, ipAddress);
        return ResponseUtil.ok("增加成功！");
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除入库源", httpMethod = "DELETE", produces = "application/json", response = CommonResult.class)
    public CommonResult deleteSource(@ApiParam(required = true, name = "id", value = "入库源id") @PathVariable("id") Integer id,
            HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        if (materialSourceService.deleteSourceById(id, userId, ipAddress)) {
            return ResponseUtil.ok("删除成功！");
        }
        return ResponseUtil.fail("删除失败！");
    }


    @GetMapping("/")
    @ApiOperation(value = "查询入库源列表", httpMethod = "GET", produces = "application/json", response = CommonResult.class)
    public CommonResult listSources(Page page) {
        return ResponseUtil.okList(materialSourceService.listSources(page));
    }


    @PutMapping("/{id}")
    @ApiOperation(value = "修改入库源", httpMethod = "PUT", produces = "application/json", response = CommonResult.class)
    public CommonResult updateSource(@ApiParam(required = true, name = "id", value = "入库源id") @PathVariable("id") Integer id,
            @RequestBody MaterialSource materialSource) {
        materialSource.setId(id);
        materialSource.setUpdateTime(new Date());
        materialSourceService.updateSource(materialSource);
        return ResponseUtil.ok("修改成功!");
    }
}
