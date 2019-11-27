package com.dazhao.controller;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.service.OperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/log")
@Api(description = "操作日志相关")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping("/")
    @ApiOperation(value = "获取查询操作日志")
    public CommonResult queryOperationLogList(@RequestParam(value = "start_time", required = false) String startTime,
            @RequestParam(value = "end_time", required = false) String endTime, Page page) {
        List<OperationLog> operationLogList = operationLogService.queryOperationLogList(page, startTime, endTime);
        return ResponseUtil.okList(operationLogList);
    }
}
