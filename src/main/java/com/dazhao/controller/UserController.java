package com.dazhao.controller;

import com.dazhao.common.DefaultEnum;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.common.utils.Sha256Util;
import com.dazhao.pojo.dao.Permission;
import com.dazhao.pojo.dao.User;
import com.dazhao.service.UserService;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "用户操作")
@RequestMapping(value = "/user")

public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登入", notes = "根据用户名和密码进行认证")
    @PostMapping(value = "/auth")
    @ApiImplicitParam(paramType = "body", name = "user", value = "管理员对象", required = true, dataType = "User")
    public CommonResult login(@RequestBody User user, HttpServletRequest request) {
        if (StringUtil.isEmpty(user.getUsername()) || StringUtil.isEmpty(user.getPassword())) {
            return ResponseUtil.loginDefeated();
        }
        User cbwmsUser = userService.findUserByName(user.getUsername());
        if (cbwmsUser == null) {
            return ResponseUtil.loginDefeated();
        }
        if (!Sha256Util.string2SHA256(user.getPassword()).equals(cbwmsUser.getPassword())) {
            return ResponseUtil.loginDefeated();
        }
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        userService.loginLog(cbwmsUser.getId(), ipAddress);
        Map<String, Object> resultMap = new HashMap<>();
        String token = JWTUtil.sign(cbwmsUser.getId());
        cbwmsUser.setPassword("");
        resultMap.put("user", cbwmsUser);
        resultMap.put("token", token);
        return ResponseUtil.ok(resultMap);
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "添加用户", notes = "超级管理员才有的权限")
    @ApiImplicitParam(paramType = "body", name = "user", value = "注册用户对象", required = true, dataType = "User")
    public CommonResult createUser(@RequestBody User user, HttpServletRequest request) {
        if (StringUtil.isEmpty(user.getUsername()) || StringUtil.isEmpty(user.getPassword()) || StringUtil.isEmpty(user.getRoleName())) {
            return ResponseUtil.badArgument();
        }
        if (!(user.getRoleName().equals(DefaultEnum.ROLE_SUPER_ADMIN.getDefaultDesc()) || user.getRoleName()
                .equals(DefaultEnum.ROLE_ADMIN.getDefaultDesc()))) {
            return ResponseUtil.badArgument();
        }
        User cbwmsUser = userService.findUserByName(user.getUsername());
        if (cbwmsUser != null) {
            return ResponseUtil.fail("-1", "登陆用户名已存在");
        }
        user.setPassword(Sha256Util.string2SHA256(user.getPassword()));
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        userService.createUser(user, userId, ipAddress);
        return ResponseUtil.ok("注册成功");
    }

    @GetMapping(value = "/user_list")
    @ApiOperation(value = "获取用户列表", notes = "超级管理员才有的权限")
    public CommonResult getUserList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @ApiParam(name = "name", value = "姓名")
            @RequestParam(value = "name", required = false) String name) {
        List<User> usersList = userService.getUserList(page, pageSize, name);
        return ResponseUtil.okList(usersList);
    }

    @DeleteMapping(value = "/deleted/{user_id}")
    @ApiOperation(value = "逻辑删除用户", notes = "超级管理员才有的权限")
    public CommonResult deletedUser(@PathVariable(value = "user_id") Integer userId, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.badArgument();
        }
        int operationId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        boolean isSuccess = userService.deleteUser(userId, operationId, ipAddress);
        if (isSuccess) {
            return ResponseUtil.ok("删除成功");
        }
        return ResponseUtil.ok("删除失败");
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改用户", notes = "超级管理员才有的权限")
    public CommonResult updateUser(@RequestBody User user, HttpServletRequest request) {
        if (!(user != null && user.getId() != null)) {
            return ResponseUtil.badArgument();
        }
        if (!StringUtils.isEmpty(user.getUsername())) {
            User cbwmsUser = userService.findUserByName(user.getUsername());
            if (cbwmsUser != null && !cbwmsUser.getId().equals(user.getId())) {
                return ResponseUtil.fail(ErrorCodeEnum.USER_EXIST.getErrorCode(), ErrorCodeEnum.USER_EXIST.getErrorMsg());
            }
        }
        if (user.getPassword() != null) {
            user.setPassword(Sha256Util.string2SHA256(user.getPassword()));
        }
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        user.setUpdateTime(new Date());
        userService.updateUserById(user, userId, ipAddress);
        return ResponseUtil.ok("更新成功");
    }

    @GetMapping(value = "/permission_list")
    @ApiOperation(value = "获取权限列表", notes = "超级管理员才有的权限")
    public CommonResult getPermissionList() {
        List<Permission> usersList = userService.getPermissionList();
        return ResponseUtil.okList(usersList);
    }

    @GetMapping(value = "/permission/{user_id}")
    @ApiOperation(value = "根据用户id权限列表", notes = "")
    public CommonResult getPermissionListByUserId(@ApiParam(name = "user_id", value = "用户id") @PathVariable(value = "user_id") Integer userId) {
        List<Permission> usersList = userService.getUserPermissionListByUserId(userId);
        return ResponseUtil.okList(usersList);
    }
}
