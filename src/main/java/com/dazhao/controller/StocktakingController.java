package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.pojo.bo.CheckBO;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.dao.StocktakingRecord;
import com.dazhao.pojo.vo.CheckMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import com.dazhao.service.StocktakingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "盘点相关接口")
@RestController
@RequestMapping(value = "stocktaking")
public class StocktakingController {

    @Autowired
    private StocktakingService stocktakingService;
    @Value("${web.upload-path}")
    private String uplodPath;
    private static final String PICTURE_PATH_SEPARATOR = "images/";

    @GetMapping("/")
    @ApiOperation(value = "查询盘点记录")
    public CommonResult queryStocktakingRecord(Page page,
            @ApiParam(name = "checkName", value = "盘点人") @RequestParam(value = "checkName", required = false) String checkName,
            @ApiParam(name = "beginTime", value = "开始时间") @RequestParam(value = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "结束时间") @RequestParam(value = "endTime", required = false) String endTime) {
        List<StocktakingRecord> stocktakingRecordList = stocktakingService.queryCheckRecords(page, checkName, beginTime, endTime);
        return ResponseUtil.okList(stocktakingRecordList);
    }

    @GetMapping("/material_group")
    @ApiOperation(value = "查询在库物资分组信息")
    public CommonResult queryExistMaterialGroup(Page page,
            @ApiParam(name = "materialName", value = "物资名称") @RequestParam(value = "materialName", required = false) String materialName,
            @ApiParam(name = "locationId", value = "仓位Id") @RequestParam(value = "locationId", required = false) Integer locationId,
            @ApiParam(name = "type", value = "查询类型默认分页，查询所有参数为1") @RequestParam(value = "type", required = false) Integer type) {
        List<MaterialGroupVO> materialGroupList = stocktakingService.queryExistMaterialGroup(page, materialName, locationId, type);
        return ResponseUtil.okList(materialGroupList);
    }

    @GetMapping("/material_group_detail")
    @ApiOperation(value = "查询盘点物资分组下的详情", notes = "需传 分类id、 实物名称、计量单位、型号、备注、仓位id")
    public CommonResult queryExistMaterialGroupDetail(Page page, MaterialGroupParameterBO materialGroupParameterBO,
            @ApiParam(name = "type", value = "查询类型默认分页，查询所有参数为1") @RequestParam(value = "type", required = false) Integer type) {
        List<CheckMaterialVO> checkMaterialVOs = stocktakingService.queryExistMaterialGroupDetail(page, materialGroupParameterBO, type);
        return ResponseUtil.okList(checkMaterialVOs);
    }

    @PostMapping("/")
    @ApiOperation(value = "添加盘点记录", notes = "物资状态 异常:6 ")
    @ApiImplicitParam(paramType = "body", name = "checkBO", value = "盘点提交对象", required = true, dataType = "CheckBO")
    public CommonResult insertStocktakingRecordAndMaterialProblem(@Validated @RequestBody CheckBO checkBO, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        stocktakingService.insertStocktakingRecordAndMaterialProblem(checkBO, userId, ipAddress);
        return ResponseUtil.ok("提交成功");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据盘点id查询盘点详情")
    public CommonResult queryStocktakingByid(@PathVariable(value = "id") Integer id) {
        StocktakingRecord stocktakingRecord = stocktakingService.queryCheckRecordsById(id);
        return ResponseUtil.ok(stocktakingRecord);

    }

    @PutMapping("/img_url")
    @ApiOperation(value = "添加盘点图片路径", notes = "只需要这两个字段{\"id\":1,\"pictureUrl\":\"xxxx\"}")
    public CommonResult updateStocktakingRecordPictureUrl(@RequestBody StocktakingRecord stocktakingRecord, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        CommonResult commonResult = stocktakingService.updateStocktakingRecordPictureUrl(stocktakingRecord, userId, ipAddress);
        if (!commonResult.isSuccess()) {
            String picturePath =
                    uplodPath + stocktakingRecord.getPictureUrl().substring(stocktakingRecord.getPictureUrl().indexOf(PICTURE_PATH_SEPARATOR))
                            .replace(PICTURE_PATH_SEPARATOR, "");
            File file = new File(picturePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return commonResult;
    }

    @DeleteMapping("/img_url")
    @ApiOperation(value = "删除盘点图片", notes = "只需要这两个字段{\"id\":1,\"pictureUrl\":\"xxxx\"}")
    public CommonResult deleteStocktakingRecordPictureUrl(@RequestBody StocktakingRecord stocktakingRecord, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        if (stocktakingRecord.getId() == null || stocktakingRecord.getPictureUrl() == null) {
            return ResponseUtil.badArgument();
        }
        String pictureUrl = stocktakingRecord.getPictureUrl();
        boolean isSuccess = stocktakingService.deleteStocktakingRecordPictureUrl(stocktakingRecord, userId, ipAddress);
        if (!isSuccess) {
            return ResponseUtil.fail("-1", "删除失败");
        }
        if (!pictureUrl.contains(PICTURE_PATH_SEPARATOR)) {
            return ResponseUtil.fail("-1", "上传图片地址错误");
        }
        String picturePath =
                uplodPath + pictureUrl.substring(pictureUrl.indexOf(PICTURE_PATH_SEPARATOR)).replace(PICTURE_PATH_SEPARATOR, "");
        File file = new File(picturePath);
        if (file.exists()) {
            if (file.delete()) {
                return ResponseUtil.ok("删除成功");
            } else {
                return ResponseUtil.fail("-1", "删除失败");
            }
        } else {
            return ResponseUtil.fail("-1", "文件不存在！");
        }
    }

}
