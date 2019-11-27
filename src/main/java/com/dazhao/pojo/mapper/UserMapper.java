package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.Permission;
import com.dazhao.pojo.dao.User;
import java.util.List;
import javax.websocket.server.PathParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UserMapper extends Mapper<User>, MySqlMapper<User> {

    /**
     * 创建用户
     *
     * @param user 用户对象
     * @return 影响条数
     */
    int createUser(User user);

    /**
     * 根据用户名称查询用户
     *
     * @param username 登录名称
     * @return 用户对象
     */
    User findUserByName(String username);

    /**
     * 获取用户信息集合
     *
     * @param name 用户姓名
     * @return 用户集合
     */
    List<User> getUserList(@Param("name") String name);

    /**
     * 根据用户id更新用户信息
     *
     * @param user 用户信息
     */
    void updateUserById(User user);

    /**
     * 逻辑删除用户
     *
     * @return 更新条数
     */
    int deleteUser(int userId);

    /**
     * 添加用户权限
     *
     * @param userId 用户id
     * @param list 权限id集合
     */
    void addUserPermission(@Param(value = "userId") Integer userId, @Param(value = "list") List<Integer> list);

    /**
     * 根据用户id获取用户权限集合
     *
     * @param userId 用户id
     * @return 用户的权限集合
     */
    List<String> getUserPermissionList(int userId);

    /**
     * 获取所有权限
     *
     * @return 权限集合
     */
    List<Permission> getPermissionList();

    /**
     * 获取用户的默认权限id集合
     *
     * @param roleType 角色类别
     * @return 权限id集合
     */
    List<Integer> defaultPermissions(@Param("roleType") Integer roleType);

    /**
     * 获取用户权限对象集合
     *
     * @param userId 用户id
     * @return 权限对象集合
     */
    List<Permission> getUserPermissionListByUserId(@Param("userId") Integer userId);
}
