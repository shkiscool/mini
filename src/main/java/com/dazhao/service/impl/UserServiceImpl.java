package com.dazhao.service.impl;

import com.dazhao.common.DefaultEnum;
import com.dazhao.common.OperationLogEnum;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.dao.Permission;
import com.dazhao.pojo.dao.User;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.pojo.mapper.UserMapper;
import com.dazhao.service.UserService;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    @Transactional
    public void createUser(User user, Integer id, String ipAddress) {
        int userId = userMapper.createUser(user);
        // 默认授权用户权限
        List<Integer> perminssionsId = null;
        if (user.getRoleName().equals(DefaultEnum.ROLE_SUPER_ADMIN.getDefaultDesc())) {
            perminssionsId = userMapper.defaultPermissions(DefaultEnum.ROLE_SUPER_ADMIN.getDefaultValue());
        } else {
            perminssionsId = userMapper.defaultPermissions(DefaultEnum.ROLE_ADMIN.getDefaultValue());
        }
        userMapper.addUserPermission(user.getId(), perminssionsId);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.CREATE_USER.getModuleName(),
                OperationLogEnum.CREATE_USER.getContent() + userId + user.getName(), new Date());
        operationLogMapper.insert(operationLog);
    }

    @Override
    public User findUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    @Override
    public List<User> getUserList(int page, int pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        List<User> userList = userMapper.getUserList(name);
        return userList;
    }

    @Override
    public void updateUserById(User user, Integer userId, String ipAddress) {
        userMapper.updateUserById(user);
        User newUser = userMapper.selectByPrimaryKey(user.getId());
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.UPDATE_USER.getModuleName(),
                OperationLogEnum.UPDATE_USER.getContent() + newUser.getId() + newUser.getName() + userId + user.getName(), new Date());
        operationLogMapper.insert(operationLog);

    }

    @Override
    public boolean deleteUser(int userId, Integer oprationId, String ipAddress) {
        boolean isSuccess = userMapper.deleteUser(userId) > 0;
        if (isSuccess) {
            User deleteUser = userMapper.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog(oprationId, ipAddress, OperationLogEnum.USER_DELETE.getModuleName(),
                    OperationLogEnum.USER_DELETE.getContent() + deleteUser.getId() + deleteUser.getName(), new Date());
            operationLogMapper.insert(operationLog);
        }
        return isSuccess;
    }

    @Override
    public List<String> getUserPermissionList(int userId) {
        return userMapper.getUserPermissionList(userId);
    }

    @Override
    public List<Permission> getPermissionList() {
        return userMapper.getPermissionList();
    }

    @Override
    public void loginLog(Integer id, String ipAddress) {
        User user = userMapper.selectByPrimaryKey(id);
        OperationLog operationLog = new OperationLog(id, ipAddress, OperationLogEnum.LOGIN_LOG.getModuleName(),
                OperationLogEnum.LOGIN_LOG.getContent(), new Date());
        operationLogMapper.insert(operationLog);
    }

    @Override
    public List<Permission> getUserPermissionListByUserId(Integer userId) {
        return userMapper.getUserPermissionListByUserId(userId);
    }
}
