package com.dazhao.controller;

import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.pojo.bo.DateStatisticsMaterialBO;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.StatisticsVo;
import com.dazhao.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "statistics")
@Api(description = "统计数据相关")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    private static final Integer APRIL_NUMBER = 4;

    @GetMapping("/source")
    @ApiOperation(value = "查询入库源物资占比数据")
    public CommonResult querySourceProportion() {
        List<StatisticsVo> statisticsProportionVos = statisticsService.querySourceProportion();
        return ResponseUtil.okList(statisticsProportionVos);
    }

    @GetMapping("/sort/{source_id}")
    @ApiOperation(value = "查询物资类别占比数据", notes = "全部渠道-》0、罚没-》1、 扣押-》2、先行登记保存-》3、主动上缴-》4")
    public CommonResult querySortProportion(@PathVariable(value = "source_id") Integer sourceId) {
        List<StatisticsVo> statisticsProportionVos = statisticsService.querySortProportion(sourceId);
        return ResponseUtil.okList(statisticsProportionVos);
    }

    @GetMapping("/trend/{time_unit}")
    @ApiOperation(value = "查询出入库趋势", notes = "按日查询 time_unit = 0,按月查询 time_unit=1,默认按日查询")
    public CommonResult queryMaterialFluctuateTrend(@PathVariable("time_unit") Integer timeUnit) {
        Map<String, List<DateStatisticsMaterialBO>> materialFluctuateTrendObject = statisticsService.materialFluctuateTrend(timeUnit);
        return ResponseUtil.ok(materialFluctuateTrendObject);
    }

    @GetMapping("/location")
    @ApiOperation(value = "统计仓位使用情况")
    public CommonResult querylocationStock() {
        List<Location> locations = statisticsService.queryLocationStock();
        return ResponseUtil.okList(locations);
    }

    @GetMapping("/bill_expire")
    @ApiOperation(value = "首页单据到期提醒数据")
    public CommonResult queryBillExpire() {
        List<BillAndMaterialVO> billAndMaterialVOS = statisticsService.queryBillExpire();
        return ResponseUtil.okList(billAndMaterialVOS);
    }

    @GetMapping("/material_expire_count")
    @ApiOperation(value = "查询销毁提醒可销毁物资总数")
    public CommonResult queryMaterialExpireCount() {
        if (((Integer) DateTime.now().getMonthOfYear()).equals(APRIL_NUMBER)) {
            int materialExpireCount = statisticsService.queryMaterialExpireCount();
            return ResponseUtil.ok(materialExpireCount);
        } else {
            return ResponseUtil.ok(null);
        }
    }

    @GetMapping("/recent_operation")
    @ApiOperation(value = "查询 最近入库、最近出库、最近盘点数据", notes = "盘点数据中有问题数量problemCount字段根据该字段判断是否有无问题")
    public CommonResult queryRecentOperation() {
        Map<String, Object> queryLastOperationObject = statisticsService.queryRecentOperation();
        return ResponseUtil.ok(queryLastOperationObject);
    }

}
