package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.Permission;
import java.util.List;

public interface AuthorizationMapper {

    List<Permission> getPermission();

    List<Permission> getPermissionList();
}
