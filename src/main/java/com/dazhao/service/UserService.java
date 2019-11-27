package com.dazhao.service;

import com.dazhao.pojo.dao.Permission;
import com.dazhao.pojo.dao.User;
import java.util.List;

public interface UserService {

    /**
     * 创建用户
     *
     * @param userId 用户id
     * @param ipAddress ip地址
     * @param user 用户对象
     */
    void createUser(User user, Integer userId, String ipAddress);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 登录用户名
     * @return 用户
     */
    User findUserByName(String username);

    /**
     * 分页查询用户信息
     *
     * @param page 页码
     * @param pageSize 数据条数
     * @param name 用户姓名
     * @return 用户集合
     */
    List<User> getUserList(int page, int pageSize, String name);


    /**
     * 根据用户id跟新用户信息
     *
     * @param user 用户信息
     * @param userId 操作id
     * @param ipAddress 操作地址
     */
    void updateUserById(User user, Integer userId, String ipAddress);

    /**
     * 根据用户id逻辑删除用户
     *
     * @param userId 用户id
     * @param oprationId 操作id
     * @param ipAddress 操作ip
     * @return 是否成功
     */
    boolean deleteUser(int userId, Integer oprationId, String ipAddress);

    /**
     * 获取用户权限集合
     *
     * @param userId 用户id
     * @return 权限路径集合
     */
    List<String> getUserPermissionList(int userId);

    /**
     * 获取权限集合
     *
     * @return 权限集合对象
     */
    List<Permission> getPermissionList();

    /**
     * 登入日志
     *
     * @param id 用户id
     * @param ipAddress 操作地址
     */
    void loginLog(Integer id, String ipAddress);

    /**
     * 获取用户权限对象集合
     *
     * @param userId 用户id
     * @return 权限对象集合
     */
    List<Permission> getUserPermissionListByUserId(Integer userId);
}
