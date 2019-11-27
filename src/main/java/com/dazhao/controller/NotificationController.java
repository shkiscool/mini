package com.dazhao.controller;

import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.pojo.dao.NotificationCondition;
import com.dazhao.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/notifications")
@Api(description = "到期提醒模块相关")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/condition")
    @ApiOperation(value = "查询到期提醒条件数据", notes = "只有一条到期提醒信息存在")
    public CommonResult getNotificationsCondition() {
        NotificationCondition notificationCondition = notificationService.getNotificationsCondition();
        return ResponseUtil.ok(notificationCondition);
    }

    @PostMapping(value = "/condition")
    @ApiOperation(value = "修改提醒条件数据", notes = "只需要传要修改的‘即将到期天数’和‘条目’无需id 只有一条提醒信息存在")
    public CommonResult updateNotificationsCondition(@RequestBody NotificationCondition notificationCondition) {
        Boolean isSuccess = notificationService.updateNotificationsCondition(notificationCondition);
        if (isSuccess) {
            return ResponseUtil.ok("更新成功");
        }
        return ResponseUtil.fail("更新失败");

    }

}
