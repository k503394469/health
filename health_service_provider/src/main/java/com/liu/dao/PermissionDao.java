package com.liu.dao;

import com.liu.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer id);
}
